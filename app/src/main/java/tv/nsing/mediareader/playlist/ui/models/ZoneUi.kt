package tv.nsing.mediareader.playlist.ui.models

data class ZoneUi(
    var id: String,
    var x: Int,
    var y: Int,
    var width: Int,
    var heigh: Int,
    var resources: List<ResourceUi> = listOf(),
)

fun ZoneUi.getZoneHeighRelation(parentHeight: Int, currentDisplayHeight: Int): Int {
    return (currentDisplayHeight * (this.heigh / (parentHeight.toDouble()))).toInt()
}

fun ZoneUi.getZoneWidthRelation(parentWidth: Int, currentDisplayWidth: Int): Int {
    return (currentDisplayWidth * (this.width / (parentWidth.toDouble()))).toInt()
}
