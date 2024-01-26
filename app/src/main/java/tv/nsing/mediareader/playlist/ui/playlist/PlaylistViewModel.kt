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
import java.io.File
import javax.inject.Inject

@HiltViewModel
class PlaylistViewModel @Inject constructor(
    private val getPlaylistFromJsonUseCase: GetPlaylistFromJsonUseCase
) : ViewModel() {

    private val _uiState = MutableLiveData<PlaylistUiState>()
    val uiState: LiveData<PlaylistUiState> = _uiState

    private val _currentPlaylist = MutableLiveData<PlaylistUi?>()
    val currentPlaylist: LiveData<PlaylistUi?> = _currentPlaylist

    init {
        _uiState.value = PlaylistUiState.Init
    }

    fun getPlaylistFromJson(dataDir: String) {
        viewModelScope.launch {
            _uiState.value = PlaylistUiState.LoadingData
            when(val result = getPlaylistFromJsonUseCase(jsonFilePath = dataDir + File.separator + Constants.RESOURCE_DIRECTORY_NAME + File.separator + Constants.EVENTS_JSON_FILE_NAME)) {
                is ResultWrapper.Error -> {
                    _uiState.value = PlaylistUiState.Error(result.message ?: "Unknown error")
                }
                is ResultWrapper.Success -> {
                    _uiState.value = PlaylistUiState.PlaylistReady(result.value)
                }
            }
        }
    }
}