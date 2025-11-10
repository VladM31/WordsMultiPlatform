package vm.words.ua.words.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.coroutines.await
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.cursor
import org.jetbrains.compose.web.css.display
import org.jetbrains.compose.web.css.minHeight
import org.jetbrains.compose.web.css.overflow
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.width
import org.jetbrains.compose.web.dom.Canvas
import org.jetbrains.compose.web.dom.Div
import org.w3c.dom.HTMLCanvasElement
import org.w3c.dom.events.WheelEvent
import kotlin.js.Promise
import kotlin.random.Random

private fun ByteArray.toUint8Array(): org.khronos.webgl.Uint8Array {
    val a = org.khronos.webgl.Uint8Array(this.size)
    for (i in indices) a.set(a, this[i].toInt() and 0xFF)
    return a
}


@JsModule("pdfjs-dist")
@JsNonModule
external object PdfJs {
    fun getDocument(params: dynamic): PdfLoadingTask
    val GlobalWorkerOptions: dynamic
}

@JsModule("pdfjs-dist/build/pdf.worker.min.mjs")
@JsNonModule
external val PdfJsWorker: dynamic

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
    onOffsetChange: (Float, Float) -> Unit
) {
    // Уникальный id канваса для привязки pdf.js
    val canvasId = remember { "pdf-canvas-${Random.nextInt(1, 1_000_000)}" }

    // Один раз на модуль: назначаем workerSrc (bundled), иначе fallback на CDN
    LaunchedEffect(Unit) {
        try {
            PdfJs.GlobalWorkerOptions.workerSrc = PdfJsWorker
        } catch (_: dynamic) {
            // fallback — будет работать без bundler import'а в dev-сборках
            PdfJs.GlobalWorkerOptions.workerSrc =
                "https://cdn.jsdelivr.net/npm/pdfjs-dist@4.7.76/build/pdf.worker.min.js"
        }
    }

    // Держим открытый документ, чтобы не грузить его на каждый ререндер страницы
    var pdf by remember(pdfData) { mutableStateOf<PdfDocument?>(null) }

    // Создаем контейнер и канвас
    Div({
        style {
            property("user-select", "none")
            overflow("hidden")
        }
    }) {
        Canvas(attrs = {
            id(canvasId)
            style {
                width(100.percent)
                // высота выставится из pdf.js через размеры viewport; но пусть будет минимальная
                minHeight(120.px)
                display(DisplayStyle.Block)
                property("background", "transparent")
                cursor("grab")
            }
        })
    }

    // Загружаем документ при смене pdfData
    LaunchedEffect(pdfData) {
        try {
            // pdf.js умеет читать Uint8Array без blob/url
            val params = js("{}")
            params.asDynamic().data = pdfData.toUint8Array()
            val task = PdfJs.getDocument(params)
            val doc = task.promise.await()
            pdf = doc
            onPageCountChanged(doc.numPages)
        } catch (t: dynamic) {
            onError(t?.message?.toString() ?: "Failed to load PDF")
        }
    }

    // Рендер выбранной страницы
    LaunchedEffect(pdf, currentPage, scale, offsetX, offsetY) {
        val doc = pdf ?: return@LaunchedEffect
        try {
            val page = doc.getPage(currentPage + 1).await()
            val cssPxScale = scale.coerceIn(0.25f, 6f) // страховка
            val viewportParams = js("{}")
            viewportParams.asDynamic().scale = cssPxScale
            val viewport = page.getViewport(viewportParams)
            val canvas = (document.getElementById(canvasId) as HTMLCanvasElement?)
                ?: return@LaunchedEffect
            val ctx = canvas.getContext("2d") ?: return@LaunchedEffect

            // devicePixelRatio для четкого рендера
            val dpr = window.devicePixelRatio
            val pixelWidth = (viewport.width as Number).toDouble()
            val pixelHeight = (viewport.height as Number).toDouble()

            canvas.width = (pixelWidth * dpr).toInt()
            canvas.height = (pixelHeight * dpr).toInt()
            canvas.style.width = "${pixelWidth}px"
            canvas.style.height = "${pixelHeight}px"

            // Сброс + масштаб DPI
            val ctx2d = ctx.asDynamic()
            ctx2d.setTransform(dpr, 0, 0, dpr, 0, 0)
            ctx2d.clearRect(0, 0, pixelWidth, pixelHeight)

            val renderTask = page.render(js("{}").also { params ->
                params.asDynamic().canvasContext = ctx
                params.asDynamic().viewport = viewport
                // Перенос сдвигаем во время рендера (панорамирование)
                params.asDynamic().transform = arrayOf(
                    1, 0, 0, 1, offsetX.toDouble(), offsetY.toDouble()
                )
            })
            renderTask.promise.await()
        } catch (t: dynamic) {
            onError(t?.message?.toString() ?: "Failed to render PDF page")
        }
    }

    // Слушатели жестов (wheel для zoom, drag для pan)
    DisposableEffect(canvasId, scale) {
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