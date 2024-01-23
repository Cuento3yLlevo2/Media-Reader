package tv.nsing.mediareader.playlist.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle

@Composable
fun PlaylistScreen(playlistViewModel: PlaylistViewModel) {

    val lifecycle = LocalLifecycleOwner.current.lifecycle

    val uiState by produceState<PlaylistUiState>(
        initialValue = PlaylistUiState.Loading,
        key1 = lifecycle,
        key2 = playlistViewModel
    ) {
        lifecycle.repeatOnLifecycle(state = Lifecycle.State.STARTED) {
            playlistViewModel.uiState.collect {
                value = it
            }
        }
    }

    when (uiState) {
        is PlaylistUiState.Error -> {

        }

        PlaylistUiState.Loading -> {
            Box(Modifier.fillMaxSize()) {
                CircularProgressIndicator(Modifier.align(Alignment.Center))
            }
        }

        is PlaylistUiState.Success -> {
            Box(Modifier.fillMaxSize()) {

                (uiState as PlaylistUiState.Success).playlists.size


            }
        }
    }

}