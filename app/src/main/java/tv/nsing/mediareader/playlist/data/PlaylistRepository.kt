package tv.nsing.mediareader.playlist.data

import tv.nsing.mediareader.playlist.data.network.response.PlaylistService
import javax.inject.Inject

class PlaylistRepository @Inject constructor(private val playlistService: PlaylistService) {

    suspend fun getMedia() {

        // if media is not downloaded
        playlistService.getMedia()
    }
}