package websocket

import org.java_websocket.client.WebSocketClient
import okhttp3.*
import com.fasterxml.jackson.module.kotlin.*
import okhttp3.MediaType.Companion.toMediaType
import org.java_websocket.handshake.ServerHandshake
import utils.log
import java.net.URI

class SocketManager : ISocketManager {
    private val LOGIN_URL = "http://modded.cubic-globe.eu:8080/login"
    private val WS_URL = "ws://modded.cubic-globe.eu:8081"
    private var token: String? = null
    private var webSocketClient: WebSocketClient? = null

    override fun connect(username: String, password: String) {
        val client = OkHttpClient()
        val requestBody = RequestBody.create(
            "application/json".toMediaType(),
            """{"username": "$username", "password": "$password"}"""
        )
        val request = Request.Builder()
            .url(LOGIN_URL)
            .post(requestBody)
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) {
                log("Login failed: ${response.body?.string()}")
                return
            }
            val responseBody = response.body?.string() ?: return
            val json = jacksonObjectMapper().readTree(responseBody)
            token = json["token"].asText()

            log("Login successful! Token: $token")
            openWebSocket()
        }
    }

    private fun openWebSocket() {
        token?.let { jwt ->
            val wsUri = URI("$WS_URL?token=$jwt")
            webSocketClient = object : WebSocketClient(wsUri) {
                override fun onOpen(handshakedata: ServerHandshake?) {
                    log("Connected to WebSocket")
                }

                override fun onMessage(message: String?) {
                    log("Received: $message")
                }

                override fun onClose(code: Int, reason: String?, remote: Boolean) {
                    log("WebSocket closed: $reason")
                }

                override fun onError(ex: Exception?) {
                    log("WebSocket error: ${ex?.message}")
                }
            }
            webSocketClient?.connect()
        } ?: log("No token available!")
    }

    override fun sendAudioData(data: ByteArray) {
        TODO("Not yet implemented")
    }

    override fun sendStringData(data: String) {
        webSocketClient?.send(data)
    }

    override fun receiveAudioData(): ByteArray {
        TODO("Not yet implemented")
    }

    override fun receiveStringData(): String {
        TODO("Not yet implemented")
    }
}