package tv.nsing.mediareader.playlist.ui.download

sealed interface DownloadUiState {
    data object Init: DownloadUiState
    data object Downloading: DownloadUiState
    data class Error(val error: String): DownloadUiState
    data object Success: DownloadUiState
}