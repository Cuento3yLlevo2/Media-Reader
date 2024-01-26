package tv.nsing.mediareader.playlist.ui.playlist

import tv.nsing.mediareader.playlist.ui.models.PlaylistUi

sealed interface PlaylistUiState {
    data object Init: PlaylistUiState
    data object LoadingData: PlaylistUiState
    data class PlaylistReady(val playlists: List<PlaylistUi>): PlaylistUiState
    data class Error(val error: String): PlaylistUiState
}
