package tv.nsing.mediareader.core

import android.app.DownloadManager
import android.content.Context
import android.os.Environment
import androidx.core.net.toUri

class ZipDownloader(private val context: Context): Downloader {

    private val downloadManager = context.getSystemService(DownloadManager::class.java)

    override fun downloadFile(url: String): Long {
        val fileTitle = "media.zip"
        val request = DownloadManager.Request(url.toUri())
            .setMimeType("application/zip")
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setTitle(fileTitle)
            .setDestinationInExternalFilesDir(context, Environment.DIRECTORY_DOWNLOADS, fileTitle)
        return downloadManager.enqueue(request)
    }
}