package tv.nsing.mediareader.playlist.ui.models

data class ZoneUi(
    var id: String,
    var x: Int,
    var y: Int,
    var width: Int,
    var height: Int,
    var resources: List<ResourceUi> = listOf(),
)

fun ZoneUi.getZoneHeightRelation(parentHeight: Int, currentDisplayHeight: Int): Int {
    return (currentDisplayHeight * (this.height / (parentHeight.toDouble()))).toInt()
}

fun ZoneUi.getZoneWidthRelation(parentWidth: Int, currentDisplayWidth: Int): Int {
    return (currentDisplayWidth * (this.width / (parentWidth.toDouble()))).toInt()
}

fun ZoneUi.getZoneXRelation(parentWidth: Int, currentDisplayWidth: Int): Int {
    return (currentDisplayWidth * (this.x / (parentWidth.toDouble()))).toInt()
}

fun ZoneUi.getZoneYRelation(parentHeight: Int, currentDisplayHeight: Int): Int {
    return (currentDisplayHeight * (this.y / (parentHeight.toDouble()))).toInt()
}
