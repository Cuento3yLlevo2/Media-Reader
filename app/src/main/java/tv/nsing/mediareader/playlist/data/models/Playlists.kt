package tv.nsing.mediareader.playlist.data.models

import com.google.gson.annotations.SerializedName
import tv.nsing.mediareader.playlist.ui.models.PlaylistUi


data class Playlists (

  @SerializedName("id") var id : String? = null,
  @SerializedName("width") var width : Int? = null,
  @SerializedName("height") var height : Int? = null,
  @SerializedName("duration") var duration : Int? = null,
  @SerializedName("zones") var zones : ArrayList<Zones> = arrayListOf()

)

fun ArrayList<Playlists>.toUiModelList(): List<PlaylistUi> {
  val playlists = mutableListOf<PlaylistUi>()
  this.forEach {
    playlists.add(it.toUiModel())
  }
  return playlists
}

fun Playlists.toUiModel(): PlaylistUi {
  return PlaylistUi(
    id = this.id ?: "",
    width = this.width ?: -1,
    height = this.height ?: -1,
    duration = this.duration ?: -1,
    zones = this.zones.toUiModelList()
  )
}