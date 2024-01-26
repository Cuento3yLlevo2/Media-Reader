package tv.nsing.mediareader.playlist.ui.download

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import tv.nsing.mediareader.playlist.ui.Routes


@Composable
fun DownloadScreen(navController: NavHostController, downloadViewModel: DownloadViewModel) {

    val downloadUiState: DownloadUiState? by downloadViewModel.uiState.observeAsState(initial = DownloadUiState.Init)

    when(downloadUiState) {
        DownloadUiState.Init -> downloadViewModel.downloadZipFile()
        DownloadUiState.Downloading -> ShowDownloadProgress(downloadViewModel)
        is DownloadUiState.Error -> ShowDownloadError(downloadViewModel, (downloadUiState as DownloadUiState.Error).error)
        DownloadUiState.Success -> {
            navController.navigate(Routes.PlaylistScreen.route) {
                popUpTo(Routes.PlaylistScreen.route) { inclusive = true }
            }
        }
        null -> {}
    }

}

@Composable
fun ShowDownloadProgress(downloadViewModel: DownloadViewModel) {
    val downloadMessage: String? by downloadViewModel.downloadState.observeAsState(initial = "")

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        CircularProgressIndicator()
        Spacer(modifier = Modifier.height(18.dp))
        Text(text = downloadMessage ?: "Downloading..", fontSize = 18.sp)
    }
}

@Composable
fun ShowDownloadError(downloadViewModel: DownloadViewModel, error: String) {

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Icon(Icons.Default.Warning, contentDescription = "download error warning icon", Modifier.size(50.dp), tint = Color.Red)
        Spacer(modifier = Modifier.height(18.dp))
        Text(text = error, fontSize = 18.sp)
        Spacer(modifier = Modifier.height(18.dp))
        Button(onClick = { downloadViewModel.downloadZipFile() }) {
            Text(text = "RETRY")
        }
    }
}
