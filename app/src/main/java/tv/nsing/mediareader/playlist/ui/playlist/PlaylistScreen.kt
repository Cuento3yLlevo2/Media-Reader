package tv.nsing.mediareader.playlist.ui.playlist

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.net.toUri
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.ui.PlayerView
import coil.compose.rememberAsyncImagePainter
import io.sanghun.compose.video.RepeatMode
import io.sanghun.compose.video.VideoPlayer
import io.sanghun.compose.video.uri.VideoPlayerMediaItem
import tv.nsing.mediareader.playlist.ui.DisplayUtils
import tv.nsing.mediareader.playlist.ui.DisplayUtils.KeepScreenOn
import tv.nsing.mediareader.playlist.ui.DisplayUtils.pxToDp
import tv.nsing.mediareader.playlist.ui.models.PlatlistMediaType
import tv.nsing.mediareader.playlist.ui.models.PlaylistUi
import tv.nsing.mediareader.playlist.ui.models.ResourceUi
import tv.nsing.mediareader.playlist.ui.models.getFilePath
import tv.nsing.mediareader.playlist.ui.models.getMediaType
import tv.nsing.mediareader.playlist.ui.models.getZoneHeightRelation
import tv.nsing.mediareader.playlist.ui.models.getZoneWidthRelation
import tv.nsing.mediareader.playlist.ui.models.getZoneXRelation
import tv.nsing.mediareader.playlist.ui.models.getZoneYRelation
import java.io.File

@Composable
fun PlaylistScreen(playlistViewModel: PlaylistViewModel) {

    val dataDir = LocalContext.current.applicationInfo.dataDir

    val playlistUiState: PlaylistUiState? by playlistViewModel.uiState.observeAsState(initial = PlaylistUiState.LoadingData)

    KeepScreenOn()

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

    val screenHeightPx = DisplayUtils.screenHeightPx()
    val screenWidthPx = DisplayUtils.screenWidthPx()

    currentPlaylist?.let { playlist ->
        Box(
            Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {
            playlist.zones.reversed()
            playlist.zones.forEach { zone ->

                val currentZoneHeightDp =
                    zone.getZoneHeightRelation(playlist.height, screenHeightPx).pxToDp()
                val currentZoneWidthDp =
                    zone.getZoneWidthRelation(playlist.width, screenWidthPx).pxToDp()

                Box(
                    Modifier
                        .height(
                            currentZoneHeightDp
                        )
                        .width(
                            currentZoneWidthDp
                        )
                        .offset(
                            x = zone
                                .getZoneXRelation(playlist.width, screenWidthPx)
                                .pxToDp(),
                            y = zone
                                .getZoneYRelation(playlist.height, screenHeightPx)
                                .pxToDp()
                        )
                        .background(color = Color.Transparent)
                ) {

                    val onMediaIndexChanged: Pair<String, Int>? by playlistViewModel.onMediaIndexChangedByZoneId.observeAsState(
                        initial = null
                    )

                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                    ) {

                        onMediaIndexChanged?.let { pair ->
                            if (pair.first == zone.id) {
                                PlayResources(zone.resources[pair.second], playlistViewModel)
                            } else {
                                playlistViewModel.getCurrentMediaIndexForZone(zone.id)?.let {
                                    PlayResources(zone.resources[it], playlistViewModel)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PlayResources(resource: ResourceUi, playlistViewModel: PlaylistViewModel) {

    when (resource.getMediaType()) {
        PlatlistMediaType.VIDEO_MEDIA_TYPE -> {
            VideoPlayerScreen(
                resource.getFilePath(LocalContext.current.applicationInfo.dataDir),
            ) {
                playlistViewModel.onUiError(it)
            }
        }

        PlatlistMediaType.IMAGE_MEDIA_TYPE -> {
            ImageScreen(resource.getFilePath(LocalContext.current.applicationInfo.dataDir)) {
                playlistViewModel.onUiError(it)
            }
        }

        null -> {
            playlistViewModel.onUiError("Error loading resource")
        }
    }
}

@Composable
fun ImageScreen(filePath: String, onError: (message: String) -> Unit) {
    val painter = rememberAsyncImagePainter(
        model = try {
            File(filePath)
        } catch (e: Exception) {
            onError(e.stackTraceToString())
        }
    )

    Image(
        painter = painter,
        contentDescription = "Playlist Image",
        contentScale = ContentScale.Crop,
    )
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
fun ShowResourceError(error: String) {

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

@Composable
fun VideoPlayerScreen(uri: String, onError: (message: String) -> Unit) {

    VideoPlayer(
        mediaItems = listOf(
            VideoPlayerMediaItem.StorageMediaItem(
                storageUri = File(uri).toUri()
            ),
        ),
        handleLifecycle = true,
        autoPlay = true,
        usePlayerController = false,
        enablePip = false,
        repeatMode = RepeatMode.ONE,
        playerInstance = { // ExoPlayer instance (Experimental)
            addListener(object : Player.Listener {
                override fun onPlayerError(error: PlaybackException) {
                    onError(error.stackTraceToString())
                }
            })


        },
        modifier = Modifier.fillMaxWidth().aspectRatio(16 / 9f),
    )

}

