package tv.nsing.mediareader.playlist.data.models

import com.google.gson.annotations.SerializedName
import tv.nsing.mediareader.playlist.ui.models.PlaylistUi


data class Events (
  @SerializedName("schedule") var schedule : Schedule? = Schedule(),
  @SerializedName("playlists") var playlists : ArrayList<Playlists> = arrayListOf()
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
    heigh = this.heigh ?: -1,
    duration = this.duration ?: -1,
    zones = this.zones.toUiModelList()
  )
}