package tv.nsing.mediareader.playlist.domain

import tv.nsing.mediareader.playlist.data.PlaylistRepository
import javax.inject.Inject

class DownloadMediaUseCase @Inject constructor(private val repository: PlaylistRepository) {

    suspend operator fun invoke() {
        return repository.getMedia()
    }
}