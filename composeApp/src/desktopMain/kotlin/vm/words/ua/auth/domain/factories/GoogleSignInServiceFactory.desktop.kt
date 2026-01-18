package vm.words.ua.auth.domain.factories

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
import vm.words.ua.auth.domain.managers.GoogleSignInManager
import vm.words.ua.auth.domain.models.GoogleSignInResult
import vm.words.ua.core.config.AppRemoteConfig
import java.awt.Desktop
import java.net.InetSocketAddress
import java.net.ServerSocket
import java.net.URI
import java.net.URLEncoder
import kotlin.coroutines.resume

/**
 * Desktop (JVM) implementation of GoogleSignInManager using OAuth 2.0 with browser.
 *
 * The auth code is exchanged for tokens via backend to keep client_secret secure.
 * Backend endpoint: POST /auth/google/exchange
 */
class GoogleSignInManagerDesktop : GoogleSignInManager {

    companion object {
        // Public Client ID (safe to expose)
        private const val CLIENT_ID =
            "75072611704-7m8iqm9sf2r26j6kvp498jlb86534aok.apps.googleusercontent.com" // Desktop client ID
        private const val AUTH_URL = "https://accounts.google.com/o/oauth2/v2/auth"
        private const val SCOPE = "email profile openid"
    }

    private val httpClient = HttpClient(OkHttp) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
            })
        }
    }

    override fun isAvailable(): Boolean {
        return Desktop.isDesktopSupported() &&
                Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)
    }

    override suspend fun signIn(): GoogleSignInResult {
        return withContext(Dispatchers.IO) {
            try {
                // Find free port for callback server
                val port = findFreePort()
                val redirectUri = "http://localhost:$port/callback"

                // Step 1: Start local server to receive callback with auth code
                val authCode = startLocalServerAndGetAuthCode(port, redirectUri)
                    ?: return@withContext GoogleSignInResult.failure("Authorization cancelled or failed")

                // Step 2: Send auth code to backend for exchange (backend has client_secret)
                val response = exchangeCodeViaBackend(authCode, redirectUri)

                if (response == null || !response.success) {
                    return@withContext GoogleSignInResult.failure(
                        response?.error ?: "Failed to exchange auth code"
                    )
                }

                GoogleSignInResult.success(
                    email = response.email ?: "",
                    displayName = response.displayName,
                    idToken = response.idToken
                )
            } catch (e: Exception) {
                GoogleSignInResult.failure("Google Sign-In failed: ${e.message}")
            }
        }
    }

    private fun findFreePort(): Int {
        return ServerSocket(0).use { it.localPort }
    }

    private suspend fun startLocalServerAndGetAuthCode(port: Int, redirectUri: String): String? {
        return suspendCancellableCoroutine { continuation ->
            var server: HttpServer? = null

            try {
                server = HttpServer.create(InetSocketAddress(port), 0)

                server.createContext("/callback") { exchange ->
                    val query = exchange.requestURI.query ?: ""
                    val params = query.split("&").associate {
                        val parts = it.split("=", limit = 2)
                        if (parts.size == 2) parts[0] to parts[1] else parts[0] to ""
                    }

                    val code = params["code"]
                    val error = params["error"]

                    // Send response to browser
                    val response = if (code != null) {
                        """
                        <html>
                        <head>
                            <title>Authorization Successful</title>
                            <style>
                                body { font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif; 
                                       text-align: center; padding-top: 50px; background: #f5f5f5; }
                                .container { background: white; padding: 40px; border-radius: 10px; 
                                            display: inline-block; box-shadow: 0 2px 10px rgba(0,0,0,0.1); }
                                h1 { color: #4CAF50; }
                            </style>
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
                    } else {
                        """
                        <html>
                        <head>
                            <title>Authorization Failed</title>
                            <style>
                                body { font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif; 
                                       text-align: center; padding-top: 50px; background: #f5f5f5; }
                                .container { background: white; padding: 40px; border-radius: 10px; 
                                            display: inline-block; box-shadow: 0 2px 10px rgba(0,0,0,0.1); }
                                h1 { color: #f44336; }
                            </style>
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
                    }

                    exchange.responseHeaders.add("Content-Type", "text/html; charset=UTF-8")
                    exchange.sendResponseHeaders(200, response.toByteArray().size.toLong())
                    exchange.responseBody.use { it.write(response.toByteArray()) }

                    // Stop server and resume coroutine
                    server?.stop(1)

                    if (!continuation.isCompleted) {
                        continuation.resume(code)
                    }
                }

                server.executor = null
                server.start()

                // Open browser with auth URL
                val authUrl = buildAuthUrl(redirectUri)
                Desktop.getDesktop().browse(URI(authUrl))

                continuation.invokeOnCancellation {
                    server?.stop(0)
                }

            } catch (e: Exception) {
                server?.stop(0)
                if (!continuation.isCompleted) {
                    continuation.resume(null)
                }
            }
        }
    }

    private fun buildAuthUrl(redirectUri: String): String {
        val params = mapOf(
            "client_id" to CLIENT_ID,
            "redirect_uri" to redirectUri,
            "response_type" to "code",
            "scope" to SCOPE,
            "access_type" to "offline",
            "prompt" to "select_account"
        )

        val queryString = params.entries.joinToString("&") { (key, value) ->
            "${URLEncoder.encode(key, "UTF-8")}=${URLEncoder.encode(value, "UTF-8")}"
        }

        return "$AUTH_URL?$queryString"
    }

    private suspend fun exchangeCodeViaBackend(code: String, redirectUri: String): GoogleExchangeResponse? {
        return try {
            httpClient.post(AppRemoteConfig.baseUrl + "/auth/google/exchange") {
                contentType(ContentType.Application.Json)
                setBody(
                    GoogleExchangeRequest(
                        code = code,
                        redirectUri = redirectUri,
                        platform = "desktop"
                    )
                )
            }.body<GoogleExchangeResponse>()
        } catch (e: Exception) {
            println("Backend token exchange error: ${e.message}")
            null
        }
    }

    override suspend fun signOut() {
        // For desktop, we just clear local state
    }
}

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

/**
 * Factory function for Desktop (JVM) platform
 */
actual fun createGoogleSignInManager(): GoogleSignInManager = GoogleSignInManagerDesktop()


