package tv.nsing.mediareader.playlist.data.models

import com.google.gson.annotations.SerializedName


data class Playlists (

  @SerializedName("id") var id : String? = null,
  @SerializedName("width") var width : Int? = null,
  @SerializedName("heigh") var heigh : Int? = null,
  @SerializedName("duration") var duration : Int? = null,
  @SerializedName("zones") var zones : ArrayList<Zones> = arrayListOf()

)