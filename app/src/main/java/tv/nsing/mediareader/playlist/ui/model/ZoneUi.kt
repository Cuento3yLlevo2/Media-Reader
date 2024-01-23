package tv.nsing.mediareader.playlist.ui.model

data class ZoneUi(
    var id: String,
    var x: Int,
    var y: Int,
    var width: Int,
    var heigh: Int,
    var resources: List<ResourceUi> = listOf()
)
