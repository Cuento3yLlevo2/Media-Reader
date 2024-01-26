package tv.nsing.mediareader

import android.app.Application
import android.app.NotificationManager
import android.content.Context

import android.os.Build
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters

import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber.DebugTree
import timber.log.Timber.Forest.plant
import tv.nsing.mediareader.playlist.data.network.response.PlaylistService
import tv.nsing.mediareader.playlist.worker.DownloadWorker
import javax.inject.Inject


@HiltAndroidApp
class MediaReaderApp : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: CustomWorkerFactory
    override fun onCreate() {
        super.onCreate()

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(DownloadWorker.getNotificationChannel())
        }

        plant(DebugTree())
    }

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setMinimumLoggingLevel(android.util.Log.DEBUG)
            .setWorkerFactory(workerFactory)
            .build()

}


class CustomWorkerFactory @Inject constructor(private val playlistService: PlaylistService): WorkerFactory() {
    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker = DownloadWorker(playlistService, context = appContext, workerParameters = workerParameters)

}