package tv.nsing.mediareader.playlist.ui.playlist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import tv.nsing.mediareader.playlist.ui.models.PlaylistUi
import kotlin.math.roundToInt

@Composable
fun PlaylistScreen(playlistViewModel: PlaylistViewModel) {

    val dataDir = LocalContext.current.applicationInfo.dataDir

    val playlistUiState: PlaylistUiState? by playlistViewModel.uiState.observeAsState(initial = PlaylistUiState.LoadingData)



    when (playlistUiState) {
        PlaylistUiState.Init -> playlistViewModel.getPlaylistFromJson(dataDir)
        is PlaylistUiState.Error -> {
            ShowPlaylistError((playlistUiState as PlaylistUiState.Error).error) {
                playlistViewModel.getPlaylistFromJson(dataDir)
            }
        }

        PlaylistUiState.LoadingData -> {
            ShowPlaylistLoading()
        }

        is PlaylistUiState.PlaylistReady -> {
            ShowPlaylist(playlistViewModel)
        }

        null -> ShowPlaylistLoading()
    }

}

@Composable
fun ShowPlaylist(playlistViewModel: PlaylistViewModel) {

    val currentPlaylist: PlaylistUi? by playlistViewModel.currentPlaylist.observeAsState(initial = null)

    currentPlaylist?.let {
        Box(
            Modifier
                .fillMaxSize()
                .background(Color.Cyan)
        ) {




        }

    }


}

@Composable
fun ShowPlaylistError(error: String, onRetry: () -> Unit) {

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Icon(
            Icons.Default.Warning,
            contentDescription = "download error warning icon",
            Modifier.size(50.dp),
            tint = Color.Red
        )
        Spacer(modifier = Modifier.height(18.dp))
        Text(text = error, fontSize = 18.sp)
        Spacer(modifier = Modifier.height(18.dp))
        Button(onClick = { onRetry() }) {
            Text(text = "RETRY")
        }
    }
}

@Composable
fun ShowPlaylistLoading() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        CircularProgressIndicator()
        Spacer(modifier = Modifier.height(18.dp))
        Text(text = "Playlist Loading..", fontSize = 18.sp)
    }
}

class CurrentDisplaySize {
    @Composable
    fun height(): Int {
        val configuration = LocalConfiguration.current
        return configuration.screenHeightDp
    }
    @Composable
    fun width(): Int {
        val configuration = LocalConfiguration.current
        return configuration.screenWidthDp
    }
    @Composable
    fun heightInPixels(): Int {
        val configuration = LocalConfiguration.current
        val screenDensity = configuration.densityDpi / 160f
        return (configuration.screenHeightDp.toFloat() * screenDensity).roundToInt()
    }
    @Composable
    fun widthInPixels(): Int {
        val configuration = LocalConfiguration.current
        val screenDensity = configuration.densityDpi / 160f
        return (configuration.screenWidthDp * screenDensity).roundToInt()
    }


}