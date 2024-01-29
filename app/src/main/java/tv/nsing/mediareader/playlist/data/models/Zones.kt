package tv.nsing.mediareader.playlist.data.models

import com.google.gson.annotations.SerializedName
import tv.nsing.mediareader.playlist.data.models.Resources
import tv.nsing.mediareader.playlist.ui.models.PlaylistUi
import tv.nsing.mediareader.playlist.ui.models.ZoneUi


data class Zones (

  @SerializedName("id") var id : String? = null,
  @SerializedName("x") var x : Int? = null,
  @SerializedName("y") var y : Int? = null,
  @SerializedName("width") var width : Int? = null,
  @SerializedName("height") var height : Int? = null,
  @SerializedName("resources") var resources : ArrayList<Resources> = arrayListOf()

)

fun ArrayList<Zones>.toUiModelList(): List<ZoneUi> {
  val zones = mutableListOf<ZoneUi>()
  this.forEach {
    zones.add(it.toUiModel())
  }
  return zones
}

fun Zones.toUiModel(): ZoneUi {
  return ZoneUi(
    id = this.id ?: "",
    width = this.width ?: -1,
    height = this.height ?: -1,
    x = this.x ?: -1,
    y = this.y ?: -1,
    resources = this.resources.toUiModelList()
  )
}