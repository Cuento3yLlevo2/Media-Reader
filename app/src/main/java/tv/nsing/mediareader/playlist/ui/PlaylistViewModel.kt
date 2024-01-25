package tv.nsing.mediareader.playlist.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import tv.nsing.mediareader.playlist.domain.DownloadMediaUseCase
import tv.nsing.mediareader.playlist.ui.model.PlaylistUi
import javax.inject.Inject

@HiltViewModel
class PlaylistViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val downloadMediaUseCase: DownloadMediaUseCase
): ViewModel() {

    val uiState: StateFlow<PlaylistUiState> =  MutableStateFlow(PlaylistUiState.Loading)

    fun checkMedia() {
        viewModelScope.launch {
            downloadMediaUseCase()
        }
    }

    private val playlists = savedStateHandle.getStateFlow("playlists", emptyList<PlaylistUi>())






}