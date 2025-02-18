package core

import ocr.IRecognitionManager
import ocr.RecognitionManager
import utils.log
import websocket.ISocketManager
import websocket.SocketManager

class AppManager(
    val ocr: IRecognitionManager = RecognitionManager(),
    val socket: ISocketManager = SocketManager()
) {
    init {
        log("AppManager initialized")
        socket.connect("testuser", "password123")
    }

    companion object {
        val instance by lazy { AppManager() }
    }
}