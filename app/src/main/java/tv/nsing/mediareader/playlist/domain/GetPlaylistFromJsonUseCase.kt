package tv.nsing.mediareader.playlist.domain

import tv.nsing.mediareader.playlist.data.PlaylistRepository
import tv.nsing.mediareader.playlist.data.ResultWrapper
import tv.nsing.mediareader.playlist.ui.models.PlaylistUi
import javax.inject.Inject

class GetPlaylistFromJsonUseCase @Inject constructor(private val repository: PlaylistRepository) {

    suspend operator fun invoke(jsonFilePath : String): ResultWrapper<List<PlaylistUi>> {
        return repository.getPlaylistFromJson(jsonFilePath = jsonFilePath)
    }
}