package vm.words.ua.words.ui.components

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.coroutines.await
import org.khronos.webgl.Uint8Array
import org.w3c.dom.HTMLCanvasElement
import org.w3c.dom.events.WheelEvent
import kotlin.js.Promise
import kotlin.random.Random

// ---------- ВСПОМОГАТЕЛЬНОЕ ----------

private fun ByteArray.toUint8Array(): Uint8Array {
    val result = Uint8Array(size)
    val dyn = result.asDynamic()
    for (i in indices) {
        dyn[i] = this[i].toInt() and 0xFF
    }
    return result
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
    fun getViewport(params: dynamic): dynamic
    fun render(params: dynamic): PdfRenderTask
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
    val canvasId = remember { "pdf-canvas-${Random.nextInt(1, 1_000_000)}" }

    // Настраиваем workerSrc
    LaunchedEffect(Unit) {
        try {
            val version = try {
                PdfJs.asDynamic().version as? String
            } catch (_: dynamic) {
                null
            } ?: "4.7.76"

            // ВАЖНО: именно pdf.worker.min.mjs, а не .js
            PdfJs.GlobalWorkerOptions.workerSrc =
                "https://cdn.jsdelivr.net/npm/pdfjs-dist@$version/build/pdf.worker.min.mjs"
        } catch (e: dynamic) {
            console.warn("PdfContent: failed to set workerSrc", e)
        }
    }

    var pdf by remember(pdfData) { mutableStateOf<PdfDocument?>(null) }

    // Создаём canvas вручную через DOM (без Compose Web)
    LaunchedEffect(Unit) {
        if (document.getElementById(canvasId) == null) {
            val container = document.getElementById("pdf-container")
                ?: document.createElement("div").apply {
                    id = "pdf-container"
                    document.body?.appendChild(this)
                }

            val canvas = document.createElement("canvas") as HTMLCanvasElement
            canvas.id = canvasId
            canvas.style.width = "100%"
            canvas.style.minHeight = "120px"
            canvas.style.background = "transparent"
            canvas.style.cursor = "grab"
            container.appendChild(canvas)
        }
    }

    // Загружаем PDF при смене данных
    LaunchedEffect(pdfData) {
        if (pdfData.isEmpty()) {
            onError("Empty PDF data")
            return@LaunchedEffect
        }
        try {
            val params = js("{}")
            params.data = pdfData.toUint8Array()
            val task = PdfJs.getDocument(params)
            val doc = task.promise.await()
            pdf = doc
            onPageCountChanged(doc.numPages)
        } catch (e: dynamic) {
            console.error("PdfContent: load error", e)
            onError(e?.message?.toString() ?: "Failed to load PDF")
        }
    }

    // Рендер текущей страницы
    LaunchedEffect(pdf, currentPage, scale, offsetX, offsetY) {
        val doc = pdf ?: return@LaunchedEffect
        try {
            val page = doc.getPage(currentPage + 1).await()
            val canvas = document.getElementById(canvasId) as? HTMLCanvasElement ?: return@LaunchedEffect
            val ctx = canvas.getContext("2d") ?: return@LaunchedEffect

            val cssScale = scale.coerceIn(0.25f, 6f)
            val viewportParams = js("{}")
            viewportParams.scale = cssScale
            val viewport = page.getViewport(viewportParams)

            val dpr = window.devicePixelRatio
            val w = (viewport.width as Number).toDouble()
            val h = (viewport.height as Number).toDouble()

            canvas.width = (w * dpr).toInt()
            canvas.height = (h * dpr).toInt()
            canvas.style.width = "${w}px"
            canvas.style.height = "${h}px"

            val ctx2d = ctx.asDynamic()
            ctx2d.setTransform(dpr, 0, 0, dpr, 0, 0)
            ctx2d.clearRect(0, 0, w, h)

            val renderParams = js("{}")
            renderParams.canvasContext = ctx
            renderParams.viewport = viewport
            renderParams.transform = arrayOf(1, 0, 0, 1, offsetX.toDouble(), offsetY.toDouble())

            page.render(renderParams).promise.await()
        } catch (e: dynamic) {
            console.error("PdfContent: render error", e)
            onError(e?.message?.toString() ?: "Failed to render page")
        }
    }

    // Зум и панорамирование
    DisposableEffect(canvasId, scale, offsetX, offsetY) {
        val canvas = document.getElementById(canvasId) as? HTMLCanvasElement
        if (canvas == null) return@DisposableEffect onDispose { }

        var dragging = false
        var lastX = 0.0
        var lastY = 0.0

        val onWheel: (dynamic) -> Unit = { ev ->
            val e = ev.unsafeCast<WheelEvent>()
            if (e.ctrlKey) {
                e.preventDefault()
                val factor = if (e.deltaY > 0) 1 / 1.1f else 1.1f
                val newScale = (scale * factor).coerceIn(0.25f, 6f)
                if (newScale != scale) onScaleChange(newScale)
            }
        }

        val onDown: (dynamic) -> Unit = { e ->
            dragging = true
            canvas.style.cursor = "grabbing"
            val ev = e.asDynamic()
            lastX = ev.clientX as Double
            lastY = ev.clientY as Double
        }

        val onUp: (dynamic) -> Unit = {
            dragging = false
            canvas.style.cursor = "grab"
        }

        val onMove: (dynamic) -> Unit = { e ->
            if (dragging) {
                val ev = e.asDynamic()
                val x = ev.clientX as Double
                val y = ev.clientY as Double
                val dx = (x - lastX).toFloat()
                val dy = (y - lastY).toFloat()
                lastX = x
                lastY = y
                onOffsetChange(offsetX + dx, offsetY + dy)
            }
        }

        canvas.addEventListener("wheel", onWheel, js("{ passive: false }"))
        canvas.addEventListener("pointerdown", onDown)
        window.addEventListener("pointerup", onUp)
        window.addEventListener("pointermove", onMove)

        onDispose {
            canvas.removeEventListener("wheel", onWheel)
            canvas.removeEventListener("pointerdown", onDown)
            window.removeEventListener("pointerup", onUp)
            window.removeEventListener("pointermove", onMove)
        }
    }
}
