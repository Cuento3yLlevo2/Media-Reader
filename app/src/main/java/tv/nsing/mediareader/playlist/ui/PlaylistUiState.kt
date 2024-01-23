package tv.nsing.mediareader.playlist.ui

import tv.nsing.mediareader.playlist.ui.model.PlaylistUi

sealed interface PlaylistUiState {
    object Loading: PlaylistUiState
    data class Error(val throwable: Throwable): PlaylistUiState
    data class Success(val playlists: List<PlaylistUi>): PlaylistUiState
}