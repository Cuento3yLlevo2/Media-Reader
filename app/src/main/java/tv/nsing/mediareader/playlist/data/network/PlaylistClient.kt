package tv.nsing.mediareader.playlist.data.network

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET

interface PlaylistClient {

    @GET("media/NSIGN_Prueba_Android.zip")
    suspend fun getMedia(): Response<ResponseBody>
}