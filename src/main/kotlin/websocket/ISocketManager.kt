package websocket

interface ISocketManager {
    fun sendAudioData(data: ByteArray)
    fun sendStringData(data: String)
    fun receiveAudioData(): ByteArray
    fun receiveStringData(): String

    fun connect(username: String, password: String)
}