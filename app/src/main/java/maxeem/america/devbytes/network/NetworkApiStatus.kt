package maxeem.america.devbytes.network

sealed class NetworkApiStatus {

    object Loading : NetworkApiStatus()
    object Success : NetworkApiStatus()

    open class Error protected constructor(val err: Throwable) : NetworkApiStatus() {
        companion object {
            fun of (err: Throwable) = when (err.javaClass) {
                in arrayOf<Class<out Throwable>>(
                        java.net.UnknownHostException::class.java,
                        java.net.ConnectException::class.java,
                        java.net.SocketTimeoutException::class.java) -> ConnectionError(err)
                else -> Error(err)
            }}
    }
    class ConnectionError(err: Throwable) : Error(err)

}