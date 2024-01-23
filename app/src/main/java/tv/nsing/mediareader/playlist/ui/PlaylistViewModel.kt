package tv.nsing.mediareader.playlist.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import tv.nsing.mediareader.playlist.domain.DownloadMediaUseCase
import javax.inject.Inject

@HiltViewModel
class PlaylistViewModel @Inject constructor(downloadMediaUseCase: DownloadMediaUseCase): ViewModel() {

    val uiState: StateFlow<PlaylistUiState> =  MutableStateFlow(PlaylistUiState.Loading)



}