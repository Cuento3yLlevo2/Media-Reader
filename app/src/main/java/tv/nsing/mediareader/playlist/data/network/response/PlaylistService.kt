package tv.nsing.mediareader.playlist.data.network.response

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import tv.nsing.mediareader.playlist.data.network.PlaylistClient
import javax.inject.Inject

class PlaylistService @Inject constructor(private val playlistClient: PlaylistClient) {

    suspend fun getMedia(): Boolean {
        return withContext(Dispatchers.IO) {
            val respose = playlistClient.getMedia()
            // todo handle this and save locally

            // true is saved correctly
            true
        }
    }
}