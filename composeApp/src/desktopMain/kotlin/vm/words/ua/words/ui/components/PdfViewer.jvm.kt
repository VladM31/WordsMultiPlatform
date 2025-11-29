package vm.words.ua.words.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.ui.layout.ContentScale
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.rendering.PDFRenderer
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import java.util.concurrent.ConcurrentHashMap

private object PdfDesktopCache {
    internal data class Entry(
        val document: PDDocument,
        val renderer: PDFRenderer,
        val renderMutex: Mutex,
        var refCount: Int,
        var activeRenders: Int = 0,
        var isClosing: Boolean = false
    )

    private val cache = ConcurrentHashMap<Int, Entry>()

    @Synchronized
    fun acquire(data: ByteArray): Entry? {
        val key = data.contentHashCode()
        val existing = cache[key]
        if (existing != null) {
            existing.refCount++
            println("DEBUG Cache: Reusing entry, refCount=${existing.refCount}")
            return existing
        }
        return try {
            val doc = PDDocument.load(ByteArrayInputStream(data))
            val entry = Entry(doc, PDFRenderer(doc), Mutex(), 1)
            cache[key] = entry
            println("DEBUG Cache: Created new entry, pages=${doc.numberOfPages}")
            entry
        } catch (e: Exception) {
            println("DEBUG Cache: Failed to load PDF: ${e.message}")
            null
        }
    }

    @Synchronized
    fun startRender(data: ByteArray): Boolean {
        val key = data.contentHashCode()
        val entry = cache[key]
        if (entry == null) {
            println("DEBUG Cache: startRender - entry not found in cache")
            return false
        }
        if (entry.isClosing) {
            println("DEBUG Cache: startRender - entry is closing")
            return false
        }
        entry.activeRenders++
        println("DEBUG Cache: startRender OK, activeRenders=${entry.activeRenders}")
        return true
    }

    @Synchronized
    fun endRender(data: ByteArray) {
        val key = data.contentHashCode()
        val entry = cache[key] ?: return
        entry.activeRenders--
        println("DEBUG Cache: endRender called, activeRenders=${entry.activeRenders}, refCount=${entry.refCount}, isClosing=${entry.isClosing}")

        if (entry.activeRenders <= 0 && entry.refCount <= 0) {
            entry.isClosing = true
            try {
                entry.document.close()
                println("DEBUG Cache: Document closed after last render finished")
            } catch (e: Exception) {
                println("DEBUG Cache: Error closing document in endRender: ${e.message}")
            }
            cache.remove(key)
        }
    }

    @Synchronized
    fun release(data: ByteArray) {
        val key = data.contentHashCode()
        val entry = cache[key] ?: return
        entry.refCount--
        println("DEBUG Cache: release called, refCount=${entry.refCount}, activeRenders=${entry.activeRenders}")

        if (entry.refCount <= 0 && entry.activeRenders <= 0) {
            entry.isClosing = true
            try {
                entry.document.close()
                println("DEBUG Cache: Document closed and removed from cache")
            } catch (e: Exception) {
                println("DEBUG Cache: Error closing document: ${e.message}")
            }
            cache.remove(key)
        } else if (entry.refCount <= 0) {
            println("DEBUG Cache: Waiting for ${entry.activeRenders} active renders to finish before closing")
        }
    }
}

@Composable
actual fun PdfContent(
    pdfData: ByteArray,
    currentPage: Int,
    scale: Float,
    offsetX: Float,
    offsetY: Float,
    onPageCountChanged: (Int) -> Unit,
    onError: (String) -> Unit,
    onScaleChange: (Float) -> Unit,
    onOffsetChange: (Float, Float) -> Unit,
    modifier: Modifier
) {
    var image by remember { mutableStateOf<BufferedImage?>(null) }
    var pageCountReported by remember { mutableStateOf(false) }
    var cacheEntry by remember { mutableStateOf<PdfDesktopCache.Entry?>(null) }

    DisposableEffect(pdfData) {
        val entry = PdfDesktopCache.acquire(pdfData)
        if (entry == null) {
            onError("Failed to load PDF")
            onPageCountChanged(0)
            return@DisposableEffect onDispose {}
        } else {
            cacheEntry = entry
            if (!pageCountReported) {
                onPageCountChanged(entry.document.numberOfPages)
                pageCountReported = true
            }
        }
        onDispose {
            image = null
            cacheEntry = null
            PdfDesktopCache.release(pdfData)
        }
    }

    LaunchedEffect(cacheEntry, currentPage) {
        val entry = cacheEntry ?: return@LaunchedEffect

        if (!PdfDesktopCache.startRender(pdfData)) {
            return@LaunchedEffect
        }

        try {
            val pageImage = entry.renderMutex.withLock {
                withContext(Dispatchers.IO) {
                    entry.renderer.renderImageWithDPI(currentPage, 144f)
                }
            }
            image = pageImage
        } catch (_: CancellationException) {
        } catch (e: Exception) {
            onError("Failed to render page: ${e.message}")
        } finally {
            PdfDesktopCache.endRender(pdfData)
        }
    }

    image?.let { img ->
        val bitmap = remember(img) { img.toComposeImageBitmap() }

        Box(
            modifier = modifier
                .fillMaxSize()
                .clipToBounds()
        ) {
            Image(
                bitmap = bitmap,
                contentDescription = "PDF Page ${currentPage + 1}",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Fit
            )
        }
    }
}
