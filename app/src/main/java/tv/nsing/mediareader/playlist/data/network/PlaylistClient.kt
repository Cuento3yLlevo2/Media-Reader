package tv.nsing.mediareader.playlist.data.network

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Streaming
import timber.log.Timber
import javax.inject.Inject

interface PlaylistClient {

    @GET("media/NSIGN_Prueba_Android.zip")
    @Streaming
    fun downloadMediaZipFile(): Call<ResponseBody>

//    @Streaming
//    @GET("media/NSIGN_Prueba_Android.zip")
//    suspend fun downloadMediaZipFile(): ResponseBody
}