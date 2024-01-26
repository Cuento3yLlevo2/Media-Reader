package tv.nsing.mediareader

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.core.content.ContextCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import tv.nsing.mediareader.core.Constants.EVENTS_JSON_FILE_NAME
import tv.nsing.mediareader.core.Constants.RESOURCE_DIRECTORY_NAME
import tv.nsing.mediareader.playlist.ui.playlist.PlaylistScreen
import tv.nsing.mediareader.playlist.ui.playlist.PlaylistViewModel
import tv.nsing.mediareader.playlist.ui.Routes
import tv.nsing.mediareader.playlist.ui.download.DownloadScreen
import tv.nsing.mediareader.playlist.ui.download.DownloadViewModel
import tv.nsing.mediareader.ui.theme.MediaReaderTheme
import java.io.File

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val playlistViewModel: PlaylistViewModel by viewModels()
    private val downloadViewModel: DownloadViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT > 32) {
            checkNotificationPermission()
        }

        setContent {
            MediaReaderTheme {

                val navigationController = rememberNavController()

                val startDestination = if (mediaFileExists()) { Routes.PlaylistScreen.route } else { Routes.DownloadScreen.route }

                NavHost(
                    navController = navigationController,
                    startDestination = startDestination
                ) {
                    composable(Routes.DownloadScreen.route) {
                        DownloadScreen(navigationController, downloadViewModel)
                    }
                    composable(Routes.PlaylistScreen.route) {
                        PlaylistScreen(playlistViewModel)
                    }
                }
            }
        }
    }

    @Composable
    private fun mediaFileExists(): Boolean {
        // Create File object with the file path
        val file =
            File(this.applicationInfo.dataDir + File.separator + RESOURCE_DIRECTORY_NAME + File.separator + EVENTS_JSON_FILE_NAME)

        // Check if file exists
        return file.exists()
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun checkNotificationPermission() {
        val permission = Manifest.permission.POST_NOTIFICATIONS
        if (ContextCompat.checkSelfPermission(
                this,
                permission
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // do nothing
        } else {
            requestNotificationPermission.launch(permission)
        }
    }

    private val requestNotificationPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (!isGranted) Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show()
        }
}