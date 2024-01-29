package tv.nsing.mediareader.playlist.data.models

import com.google.gson.annotations.SerializedName
import tv.nsing.mediareader.playlist.ui.models.PlaylistUi


data class Events(
    @SerializedName("schedule") var schedule: Schedule? = Schedule(),
    @SerializedName("playlists") var playlists: ArrayList<Playlists> = arrayListOf()
)