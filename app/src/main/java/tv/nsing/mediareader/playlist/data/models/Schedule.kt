package tv.nsing.mediareader.playlist.data.models

import com.google.gson.annotations.SerializedName


data class Schedule (

  @SerializedName("id") var id : String? = null,
  @SerializedName("name") var name : String? = null

)