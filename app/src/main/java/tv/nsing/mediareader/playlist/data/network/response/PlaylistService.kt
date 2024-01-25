package tv.nsing.mediareader.playlist.data.network.response

import okhttp3.ResponseBody
import retrofit2.Call
import timber.log.Timber
import tv.nsing.mediareader.playlist.data.network.PlaylistClient
import tv.nsing.mediareader.playlist.data.network.NetworkResult
import javax.inject.Inject

class PlaylistService @Inject constructor(private val playlistClient: PlaylistClient) {

    fun downloadMediaZipFile(): NetworkResult<Call<ResponseBody>> {
        return try {
            NetworkResult.Success(
                playlistClient.downloadMediaZipFile()
            )
        } catch (ex: Exception) {
            Timber.tag(TAG).e(ex.localizedMessage)
            NetworkResult.Error(ex.localizedMessage)
        }
    }

    companion object {
        const val TAG = "PlaylistService"
    }

}