package tv.nsing.mediareader.playlist.data.network


sealed class NetworkResult<out T: Any>{
    data class Success<out T: Any>(val value: T): NetworkResult<T>()
    data class Error(val message: String?, val statusCode: Int? = null, val throwable: Throwable? = null) : NetworkResult<Nothing>()
}
