package tv.nsing.mediareader

import android.app.Application
import android.app.NotificationManager
import android.os.Build
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber.DebugTree
import timber.log.Timber.Forest.plant
import tv.nsing.mediareader.playlist.worker.DownloadWorker


@HiltAndroidApp
class MediaReaderApp : Application() {

    override fun onCreate() {
        super.onCreate()

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(DownloadWorker.getNotificationChannel())
        }

        plant(DebugTree())
    }

}