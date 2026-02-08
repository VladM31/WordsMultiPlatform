package vm.words.ua.auth.domain.factories

import com.sun.net.httpserver.HttpExchange
import com.sun.net.httpserver.HttpServer
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import vm.words.ua.auth.domain.managers.GoogleApiManager
import vm.words.ua.auth.domain.models.google.GoogleSignInResult
import vm.words.ua.core.config.AppRemoteConfig
import java.awt.Desktop
import java.net.InetSocketAddress
import java.net.ServerSocket
import java.net.URI
import java.net.URLEncoder
import kotlin.coroutines.resume

/**
 * Desktop (JVM) implementation of [GoogleApiManager] using OAuth 2.0 with browser.
 *
 * Flow:
 * 1. Opens browser for Google authentication
 * 2. Receives auth code via local callback server
 * 3. Exchanges code for tokens via backend (keeps client_secret secure)
 *
 * Backend endpoint: `POST /auth/google/exchange`
 */
class GoogleApiManagerDesktop : GoogleApiManager {

    private val httpClient by lazy { createHttpClient() }

    override fun isAvailable(): Boolean =
        Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)

    override suspend fun signIn(): GoogleSignInResult = withContext(Dispatchers.IO) {
        runCatching {
            val port = findFreePort()
            val redirectUri = "$LOCALHOST_URL:$port$CALLBACK_PATH"

            val authCode = awaitAuthCode(port, redirectUri)
                ?: return@withContext GoogleSignInResult.failure("Authorization cancelled or failed")

            val response = exchangeCodeViaBackend(authCode, redirectUri)
                ?: return@withContext GoogleSignInResult.failure("Failed to exchange auth code")

            if (!response.success) {
                return@withContext GoogleSignInResult.failure(response.error ?: "Unknown error")
            }

            GoogleSignInResult.success(
                email = response.email.orEmpty(),
                displayName = response.displayName,
                idToken = response.idToken
            )
        }.getOrElse { e ->
            GoogleSignInResult.failure("Google Sign-In failed: ${e.message}")
        }
    }

    override suspend fun signOut() {
        // Desktop: no persistent session to clear
    }

    // ==================== OAuth Flow ====================

    private suspend fun awaitAuthCode(port: Int, redirectUri: String): String? =
        suspendCancellableCoroutine { continuation ->
            var server: HttpServer? = null

            runCatching {
                server = HttpServer.create(InetSocketAddress(port), 0).apply {
                    createContext(CALLBACK_PATH) { exchange ->
                        val code = exchange.handleCallback()
                        stop(1)
                        if (!continuation.isCompleted) {
                            continuation.resume(code)
                        }
                    }
                    executor = null
                    start()
                }

                Desktop.getDesktop().browse(URI(buildAuthUrl(redirectUri)))

                continuation.invokeOnCancellation { server?.stop(0) }
            }.onFailure {
                server?.stop(0)
                if (!continuation.isCompleted) {
                    continuation.resume(null)
                }
            }
        }

    private fun HttpExchange.handleCallback(): String? {
        val params = parseQueryParams()
        val code = params["code"]
        val error = params["error"]

        sendHtmlResponse(
            if (code != null) successHtml() else errorHtml(error)
        )

        return code
    }

    private fun HttpExchange.parseQueryParams(): Map<String, String> =
        requestURI.query
            ?.split("&")
            ?.mapNotNull { param ->
                param.split("=", limit = 2)
                    .takeIf { it.size == 2 }
                    ?.let { it[0] to it[1] }
            }
            ?.toMap()
            ?: emptyMap()

    private fun HttpExchange.sendHtmlResponse(html: String) {
        val bytes = html.toByteArray(Charsets.UTF_8)
        responseHeaders["Content-Type"] = "text/html; charset=UTF-8"
        sendResponseHeaders(200, bytes.size.toLong())
        responseBody.use { it.write(bytes) }
    }

    // ==================== Backend Communication ====================

    private suspend fun exchangeCodeViaBackend(
        code: String,
        redirectUri: String
    ): GoogleExchangeResponse? = runCatching {
        httpClient.post("${AppRemoteConfig.baseUrl}/auth/google/exchange") {
            contentType(ContentType.Application.Json)
            setBody(
                GoogleExchangeRequest(
                    code = code,
                    redirectUri = redirectUri,
                    platform = PLATFORM
                )
            )
        }.body<GoogleExchangeResponse>()
    }.onFailure {
        println("Backend token exchange error: ${it.message}")
    }.getOrNull()

    // ==================== URL Building ====================

    private fun buildAuthUrl(redirectUri: String): String {
        val params = mapOf(
            "client_id" to CLIENT_ID,
            "redirect_uri" to redirectUri,
            "response_type" to "code",
            "scope" to SCOPE,
            "access_type" to "offline",
            "prompt" to "select_account"
        )
        return "$AUTH_URL?${params.toQueryString()}"
    }

    private fun Map<String, String>.toQueryString(): String =
        entries.joinToString("&") { (key, value) ->
            "${key.urlEncode()}=${value.urlEncode()}"
        }

    private fun String.urlEncode(): String = URLEncoder.encode(this, Charsets.UTF_8)

    // ==================== Helpers ====================

    private fun findFreePort(): Int = ServerSocket(0).use { it.localPort }

    private fun createHttpClient() = HttpClient(OkHttp) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
            })
        }
    }

    // ==================== HTML Templates ====================

    private fun successHtml() = """
        <!DOCTYPE html>
        <html lang="en">
        <head>
            <meta charset="UTF-8">
            <title>Authorization Successful</title>
            <style>${baseStyles()} h1 { color: #4CAF50; }</style>
        </head>
        <body>
            <div class="container">
                <h1>✓ Authorization Successful</h1>
                <p>You can close this window and return to the application.</p>
            </div>
            <script>setTimeout(() => window.close(), 2000);</script>
        </body>
        </html>
    """.trimIndent()

    private fun errorHtml(error: String?) = """
        <!DOCTYPE html>
        <html lang="en">
        <head>
            <meta charset="UTF-8">
            <title>Authorization Failed</title>
            <style>${baseStyles()} h1 { color: #f44336; }</style>
        </head>
        <body>
            <div class="container">
                <h1>✗ Authorization Failed</h1>
                <p>Error: ${error ?: "Unknown error"}</p>
                <p>Please close this window and try again.</p>
            </div>
        </body>
        </html>
    """.trimIndent()

    private fun baseStyles() = """
        body {
            font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
            text-align: center;
            padding-top: 50px;
            background: #f5f5f5;
            margin: 0;
        }
        .container {
            background: white;
            padding: 40px;
            border-radius: 10px;
            display: inline-block;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
        }
    """

    companion object {
        private const val CLIENT_ID = "1005445939198-gn7d0rv3tutr8fdcapecj5fk2uvj0tib.apps.googleusercontent.com"
        private const val AUTH_URL = "https://accounts.google.com/o/oauth2/v2/auth"
        private const val SCOPE = "email profile openid"
        private const val LOCALHOST_URL = "http://localhost"
        private const val CALLBACK_PATH = "/callback"
        private const val PLATFORM = "desktop"
    }
}

// ==================== DTOs ====================

@Serializable
private data class GoogleExchangeRequest(
    val code: String,
    val redirectUri: String,
    val platform: String
)

@Serializable
private data class GoogleExchangeResponse(
    val success: Boolean,
    val email: String? = null,
    val displayName: String? = null,
    val idToken: String? = null,
    val error: String? = null
)

// ==================== Factory ====================

actual fun createGoogleApiManager(): GoogleApiManager = GoogleApiManagerDesktop()