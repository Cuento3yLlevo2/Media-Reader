package tv.nsing.mediareader

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.work.ExistingWorkPolicy
import androidx.work.WorkInfo
import androidx.work.WorkManager
import tv.nsing.mediareader.playlist.ui.PlaylistScreen
import tv.nsing.mediareader.playlist.ui.PlaylistViewModel
import tv.nsing.mediareader.playlist.worker.DownloadWorker
import tv.nsing.mediareader.playlist.worker.WorkerKeys
import tv.nsing.mediareader.ui.theme.MediaReaderTheme

class MainActivity : ComponentActivity() {

    private val playlistViewModel: PlaylistViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val downloadRequest = DownloadWorker.createWorkRequest()
        val workManager = WorkManager.getInstance(applicationContext)


        setContent {
            MediaReaderTheme {

                val workInfos =
                    workManager.getWorkInfosForUniqueWorkLiveData(DownloadWorker.WORK_NAME)
                        .observeAsState()
                        .value

                val downloadInfo = remember(key1 = workInfos) {
                    workInfos?.find { it.id == downloadRequest.id }
                }

                val mediaFolderPath by remember { derivedStateOf {
                    val downloadPath = downloadInfo?.outputData?.getString(WorkerKeys.MEDIA_URI)
                }
                }



                Column {
                    Button(onClick = {
                        workManager.beginUniqueWork(
                            DownloadWorker.WORK_NAME,
                            ExistingWorkPolicy.KEEP,
                            downloadRequest
                        ).enqueue()
                    }, enabled = downloadInfo?.state != WorkInfo.State.RUNNING
                    ) {
                        Text("start work")
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    when(downloadInfo?.state) {
                        WorkInfo.State.ENQUEUED -> Text(text = "ENQUEUED")
                        WorkInfo.State.RUNNING -> Text(text = "RUNNING")
                        WorkInfo.State.SUCCEEDED -> Text(text = "SUCCEEDED")
                        WorkInfo.State.FAILED -> Text(text = "FAILED")
                        WorkInfo.State.BLOCKED -> Text(text = "BLOCKED")
                        WorkInfo.State.CANCELLED -> Text(text = "CANCELLED")
                        null -> {

                        }
                    }
                }

                // PlaylistScreen(playlistViewModel)
            }
        }
    }
}