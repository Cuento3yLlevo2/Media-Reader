package tv.nsing.mediareader.playlist.data.local

import com.google.gson.Gson
import tv.nsing.mediareader.playlist.data.models.Events
import tv.nsing.mediareader.playlist.data.models.toUiModelList
import tv.nsing.mediareader.playlist.ui.models.PlaylistUi
import java.io.File
import javax.inject.Inject

class PlaylistLocal @Inject constructor(private val gson: Gson) {

    fun getPlaylistFromJson(jsonFilePath : String): List<PlaylistUi> {
        val inputString = File(jsonFilePath).bufferedReader().use {
            it.readText()
        }
        val events = gson.fromJson(inputString, Events::class.java)
        return events.playlists.toUiModelList()
    }

}