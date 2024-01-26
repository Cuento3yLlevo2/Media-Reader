package tv.nsing.mediareader.playlist.data


sealed class ResultWrapper<out T: Any>{
    data class Success<out T: Any>(val value: T): ResultWrapper<T>()
    data class Error(val message: String?, val statusCode: Int? = null, val throwable: Throwable? = null) : ResultWrapper<Nothing>()
}
