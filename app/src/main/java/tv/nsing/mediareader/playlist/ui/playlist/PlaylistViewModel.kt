package tv.nsing.mediareader.playlist.ui.playlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import tv.nsing.mediareader.core.Constants
import tv.nsing.mediareader.playlist.data.ResultWrapper
import tv.nsing.mediareader.playlist.domain.GetPlaylistFromJsonUseCase
import tv.nsing.mediareader.playlist.ui.models.PlaylistUi
import tv.nsing.mediareader.playlist.ui.models.ResourceUi
import java.io.File
import java.util.Timer
import javax.inject.Inject
import kotlin.concurrent.timerTask

@HiltViewModel
class PlaylistViewModel @Inject constructor(
    private val getPlaylistFromJsonUseCase: GetPlaylistFromJsonUseCase,
) : ViewModel() {

    private val _uiState = MutableLiveData<PlaylistUiState>()
    val uiState: LiveData<PlaylistUiState> = _uiState

    private val _currentPlaylist = MutableLiveData<PlaylistUi?>()
    val currentPlaylist: LiveData<PlaylistUi?> = _currentPlaylist

    private val _currentMediaIndexByZoneId = MutableLiveData<MutableMap<String, Int>>()

    private val _onMediaIndexChangedByZoneId = MutableLiveData<Pair<String, Int>>()
    val onMediaIndexChangedByZoneId: LiveData<Pair<String, Int>> = _onMediaIndexChangedByZoneId

    private val playlistTimer: Timer = Timer()
    private var resourcesTimer: Timer? = null

    init {
        resourcesTimer = Timer()
        _currentMediaIndexByZoneId.value = mutableMapOf()
        _uiState.value = PlaylistUiState.Init

    }

    fun getPlaylistFromJson(dataDir: String) {
        viewModelScope.launch {
            _uiState.value = PlaylistUiState.LoadingData
            when (val result =
                getPlaylistFromJsonUseCase(jsonFilePath = dataDir + File.separator + Constants.RESOURCE_DIRECTORY_NAME + File.separator + Constants.EVENTS_JSON_FILE_NAME)) {
                is ResultWrapper.Error -> {
                    _uiState.value = PlaylistUiState.Error(result.message ?: "Unknown error")
                }

                is ResultWrapper.Success -> {
                    if (result.value.isNotEmpty()) {
                        preparePlaylists(result.value, 0)
                    } else {
                        _uiState.value = PlaylistUiState.Error("There is not playlist to play")
                    }

                }
            }
        }
    }

    private fun preparePlaylists(playlists: List<PlaylistUi>, currentIndex: Int) {
        playlists[currentIndex].let {

            it.zones.forEach { zone ->

                prepareZoneResources(0, zone.id, zone.resources)
            }

            _currentPlaylist.postValue(it)

            playlistTimer.schedule(timerTask {
                _uiState.postValue(PlaylistUiState.LoadingData)

                resourcesTimer?.cancel()
                resourcesTimer = Timer()

                val nextIndex = currentIndex + 1
                if (nextIndex in playlists.indices) {
                    preparePlaylists(playlists, nextIndex)
                } else {
                    preparePlaylists(playlists, 0)
                }
            }, (it.duration * 1000L))
        }

        _uiState.postValue(PlaylistUiState.PlaylistReady)
    }

    private fun prepareZoneResources(
        currentResourceIndex: Int,
        zoneId: String,
        resources: List<ResourceUi>,
    ) {
        resourcesTimer?.schedule(timerTask {
            val nextIndex = currentResourceIndex + 1
            if (nextIndex in resources.indices) {
                prepareZoneResources(
                    nextIndex,
                    zoneId,
                    resources,
                )
            } else {
                prepareZoneResources(0, zoneId, resources)
            }

        }, (resources[currentResourceIndex].duration * 1000L))

        _onMediaIndexChangedByZoneId.postValue(zoneId to currentResourceIndex)
        _currentMediaIndexByZoneId.value?.set(zoneId, currentResourceIndex)
    }

    fun getCurrentMediaIndexForZone(id: String): Int? {
        return _currentMediaIndexByZoneId.value?.get(id)
    }

    fun onUiError(it: String) {
        viewModelScope.launch {
            _uiState.value = PlaylistUiState.Error(it)
        }

    }
}