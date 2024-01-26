package tv.nsing.mediareader.playlist.ui.models

data class PlaylistUi(
    val id: String,
    val width: Int,
    val heigh: Int,
    val duration: Int,
    val zones: List<ZoneUi> = listOf()
)
