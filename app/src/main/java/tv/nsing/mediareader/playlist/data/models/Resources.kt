package tv.nsing.mediareader.playlist.data.models

import com.google.gson.annotations.SerializedName
import tv.nsing.mediareader.playlist.ui.models.ResourceUi
import tv.nsing.mediareader.playlist.ui.models.ZoneUi


data class Resources (

  @SerializedName("id") var id : String? = null,
  @SerializedName("order") var order : Int? = null,
  @SerializedName("name") var name : String? = null,
  @SerializedName("duration") var duration : Int? = null

)

fun ArrayList<Resources>.toUiModelList(): List<ResourceUi> {
  val resources = mutableListOf<ResourceUi>()
  this.forEach {
    resources.add(it.toUiModel())
  }
  return resources
}

fun Resources.toUiModel(): ResourceUi {
  return ResourceUi(
    id = this.id ?: "",
    order = this.order ?: -1,
    name = this.name ?: "",
    duration = this.duration ?: -1
  )
}