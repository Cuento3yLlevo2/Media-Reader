package tv.nsing.mediareader.playlist.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import tv.nsing.mediareader.playlist.data.local.PlaylistLocal
import tv.nsing.mediareader.playlist.ui.models.PlaylistUi
import javax.inject.Inject

class PlaylistRepository @Inject constructor(private val playlistLocal: PlaylistLocal) {

    suspend fun getPlaylistFromJson(jsonFilePath : String): ResultWrapper<List<PlaylistUi>> {
        return withContext(Dispatchers.IO) {
            try {
                val result = playlistLocal.getPlaylistFromJson(jsonFilePath = jsonFilePath)
                ResultWrapper.Success(result)
            } catch (e: Exception) {
                Timber.e(e.stackTraceToString())
                ResultWrapper.Error(e.localizedMessage, throwable = e)
            }
        }
    }
}