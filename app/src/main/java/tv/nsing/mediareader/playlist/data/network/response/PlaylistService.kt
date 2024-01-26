package tv.nsing.mediareader.playlist.data.network.response

import okhttp3.ResponseBody
import retrofit2.Call
import timber.log.Timber
import tv.nsing.mediareader.playlist.data.network.PlaylistClient
import tv.nsing.mediareader.playlist.data.ResultWrapper
import javax.inject.Inject

class PlaylistService @Inject constructor(private val playlistClient: PlaylistClient) {

    fun downloadMediaZipFile(): ResultWrapper<Call<ResponseBody>> {
        return try {
            ResultWrapper.Success(
                playlistClient.downloadMediaZipFile()
            )
        } catch (ex: Exception) {
            Timber.tag(TAG).e(ex.localizedMessage)
            ResultWrapper.Error(ex.localizedMessage)
        }
    }

    companion object {
        const val TAG = "PlaylistService"
    }

}