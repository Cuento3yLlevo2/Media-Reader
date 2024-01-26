package tv.nsing.mediareader.playlist.ui

sealed class Routes(val route: String) {
    data object DownloadScreen: Routes("download")
    data object PlaylistScreen: Routes("playlist")
}