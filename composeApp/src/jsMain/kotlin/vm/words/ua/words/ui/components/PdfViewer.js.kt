package vm.words.ua.words.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.coroutines.await
import org.khronos.webgl.Uint8Array
import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.HTMLCanvasElement
import kotlin.js.Promise
import org.jetbrains.skia.Image as SkiaImage

// ---------- ВСПОМОГАТЕЛЬНОЕ ----------

private fun ByteArray.toUint8Array(): Uint8Array =
    Uint8Array(this.toTypedArray())

private fun dataUrlToBytes(dataUrl: String): ByteArray {
    val commaIndex = dataUrl.indexOf(',')
    if (commaIndex < 0) return ByteArray(0)
    val base64 = dataUrl.substring(commaIndex + 1)
    // стандартный браузерный atob
    val binary = window.atob(base64)
    val bytes = ByteArray(binary.length)
    for (i in binary.indices) {
        bytes[i] = binary[i].code.toByte()
    }
    return bytes
}

private fun dataUrlToImageBitmap(dataUrl: String): ImageBitmap {
    val bytes = dataUrlToBytes(dataUrl)
    if (bytes.isEmpty()) error("Empty PNG data from canvas")
    val skiaImage = SkiaImage.makeFromEncoded(bytes)
    return skiaImage.toComposeImageBitmap()
}

// ---------- pdf.js externals ----------

@JsModule("pdfjs-dist")
@JsNonModule
external object PdfJs {
    fun getDocument(params: dynamic): PdfLoadingTask
    val GlobalWorkerOptions: dynamic
    val version: String
}

external interface PdfLoadingTask {
    val promise: Promise<PdfDocument>
    fun destroy()
}

external interface PdfDocument {
    val numPages: Int
    fun getPage(pageNumber: Int): Promise<PdfPage>
    fun destroy()
}

external interface PdfPage {
    fun getViewport(params: dynamic): PdfViewport
    fun render(params: dynamic): PdfRenderTask
}

external interface PdfViewport {
    val width: Number
    val height: Number
}

external interface PdfRenderTask {
    val promise: Promise<Unit>
    fun cancel()
}

// ---------- COMPOSABLE ----------

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
    var pdf by remember { mutableStateOf<PdfDocument?>(null) }
    var pageBitmap by remember { mutableStateOf<ImageBitmap?>(null) }

    // workerSrc
    LaunchedEffect(Unit) {
        try {
            val version = try {
                PdfJs.version
            } catch (e: dynamic) {
                "4.7.76"
            }
            PdfJs.GlobalWorkerOptions.workerSrc =
                "https://cdn.jsdelivr.net/npm/pdfjs-dist@$version/build/pdf.worker.min.mjs"
        } catch (e: dynamic) {
            console.warn("PdfContent: failed to set workerSrc", e)
        }
    }

    // загрузка PDF
    LaunchedEffect(pdfData) {
        pdf?.destroy()
        pdf = null
        pageBitmap = null

        if (pdfData.isEmpty()) {
            onError("Empty PDF data")
            return@LaunchedEffect
        }
        try {
            val params = js("({})")
            params.data = pdfData.toUint8Array()
            val task = PdfJs.getDocument(params)
            val doc = task.promise.await()
            pdf = doc
            onPageCountChanged(doc.numPages)
        } catch (e: Throwable) {
            if (e.message?.contains("coroutine scope left the composition") == true) {
                return@LaunchedEffect
            }
            console.error("PdfContent: load error", e)
            onError(e.message ?: "Failed to load PDF")
        }
    }

    // рендер страницы в ImageBitmap
    LaunchedEffect(pdf, currentPage, scale) {
        val doc = pdf ?: return@LaunchedEffect
        if (currentPage !in 0 until doc.numPages) return@LaunchedEffect

        try {
            val page = doc.getPage(currentPage + 1).await()

            val cssScale = scale.coerceIn(0.25f, 6f)
            val viewportParams = js("({})")
            viewportParams.scale = cssScale

            val viewport = page.getViewport(viewportParams)
            val w = viewport.width.toDouble()
            val h = viewport.height.toDouble()

            val canvas = document.createElement("canvas") as HTMLCanvasElement
            canvas.width = w.toInt()
            canvas.height = h.toInt()

            val ctx = canvas.getContext("2d") as? CanvasRenderingContext2D
                ?: return@LaunchedEffect

            val renderParams = js("({})")
            renderParams.canvasContext = ctx
            renderParams.viewport = viewport

            page.render(renderParams).promise.await()

            val dataUrl = canvas.toDataURL("image/png")
            pageBitmap = dataUrlToImageBitmap(dataUrl)
        } catch (e: Throwable) {
            if (e.message?.contains("coroutine scope left the composition") == true) {
                // старая корутина отменилась из-за нового scale/страницы — это ок
                return@LaunchedEffect
            }
            console.error("PdfContent: render error", e)
            onError(e.message ?: "Failed to render page")
        }
    }

    // offset пока не используем, но сигнатуру поддерживаем
    DisposableEffect(Unit) {
        onOffsetChange(0f, 0f)
        onDispose { pdf?.destroy() }
    }

    // обычный Compose UI: LazyColumn его нормально скроллит
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        val bmp = pageBitmap
        if (bmp != null) {
            Image(
                bitmap = bmp,
                contentDescription = null,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}
