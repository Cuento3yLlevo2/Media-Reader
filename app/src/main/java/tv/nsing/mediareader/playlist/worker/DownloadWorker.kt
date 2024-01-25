package tv.nsing.mediareader.playlist.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.ForegroundInfo
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.google.gson.GsonBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import tv.nsing.mediareader.R
import tv.nsing.mediareader.playlist.data.network.NetworkResult
import tv.nsing.mediareader.playlist.data.network.PlaylistClient
import tv.nsing.mediareader.playlist.data.network.response.PlaylistService
import tv.nsing.mediareader.playlist.worker.WorkerKeys.ERROR_MSG
import tv.nsing.mediareader.playlist.worker.WorkerKeys.MEDIA_URI
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.net.ConnectException
import java.net.UnknownHostException
import java.util.zip.ZipFile
import java.util.zip.ZipInputStream
import javax.inject.Inject
import kotlin.random.Random


class DownloadWorker (
    private val context: Context,
    private val workerParameters: WorkerParameters
) : CoroutineWorker(context, workerParameters) {

    private var appDirectory: String? = null
    private lateinit var playlistService: PlaylistService

    @Inject
    lateinit var playlistClient: PlaylistClient

    override suspend fun doWork(): Result {
        // startForegroundService()

        val gson = GsonBuilder()
            .setLenient()
            .create()

        appDirectory = context.applicationInfo.dataDir
        playlistService = PlaylistService(
            Retrofit.Builder()
            .baseUrl("https://media.nsign.tv/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build().create(PlaylistClient::class.java))

        Timber.tag(TAG).i("------------------------------")
        Timber.tag(TAG).i("workerID: $id")

        when (val result = playlistService.downloadMediaZipFile()) {
            is NetworkResult.Error -> {
                result.throwable?.let { throwable ->
                    if (throwable is UnknownHostException || throwable is ConnectException) {
                        // It was a network problem: there is not Internet connection, or this is not active
                        Timber.tag(TAG)
                            .e("There is not internet connection!")
                    } else {
                        // There was other problems, probably related to the server: time out (SocketTimeoutException) or errors during the process the calls from the client
                        Timber.tag(TAG).e("The server is not available!")
                    }
                } ?: Timber.tag(TAG).e("There was an error saving the file - ${result.message}")
                return Result.failure(dataOnFailure("There was a network error on downloading the file"))
            }
            is NetworkResult.Success -> {
                val response = result.value.execute()

                Timber.tag(TAG).i("downloading zip")

                if (!response.isSuccessful && response.errorBody() != null) {

                    if (response.code().toString().startsWith("5")) {
                        Timber.tag(TAG).e("There was a server error - retrying")
                        return Result.retry()
                    }

                    if (response.code() != 404) {
                        Timber.tag(TAG).e("There was an error on downloading the file")
                    } else {
                        Timber.tag(TAG).e("Error 404: file not found!")
                    }

                    return Result.failure(dataOnFailure("There was a network error on downloading the file"))

                } else {
                    response.body().use { body ->
                        body?.let {
                            return withContext(Dispatchers.IO) {

                                val fileSize: Long = saveResource(
                                    responseBody = it,
                                )

                                if (fileSize == 0L) {
                                    Timber.tag(TAG).e("There was an error saving the file - fileSize == 0L")
                                    return@withContext Result.failure(dataOnFailure("here was an error saving the file"))
                                } else {
                                    // todo save on database
                                }

                                return@withContext Result.success(dataOnSuccess(appDirectory + File.separator + RESOURCE_DIRECTORY))

                            }
                        } ?: run {
                            Timber.tag(TAG)
                                .e("There was an error saving the file - responseJSON.body() is null")
                            return Result.failure(dataOnFailure("here was an error saving the file"))
                        }
                    }
                }
            }
        }
    }

    private fun saveResource(responseBody: ResponseBody): Long {
        try {

            val resourceDirectory = File(
                appDirectory + File.separator + RESOURCE_DIRECTORY
            )

            // This line creates the "resource" folder
            resourceDirectory.mkdirs()

            Timber.tag(TAG).i("saving zip on resourceDirectory $resourceDirectory")

            val fileReader = ByteArray(4096)
            var fileSizeDownloaded: Long = 0

            responseBody.byteStream().use { inputStream ->
                FileOutputStream(
                    resourceDirectory.toString() + File.separator + RESOURCE_DIRECTORY + ".zip"
                ).use { outputStream ->
                    while (true) {
                        val read = inputStream.read(fileReader)
                        if (read == -1) {
                            break
                        }
                        outputStream.write(fileReader, 0, read)
                        fileSizeDownloaded += read.toLong()
                    }
                    outputStream.flush()

                    // to unzip the file
                    val zipFile = File(
                        resourceDirectory.toString() + File.separator + RESOURCE_DIRECTORY + ".zip"
                    )
                    fileSizeDownloaded = unZipFile(zipFile, resourceDirectory)
                }
            }

            return fileSizeDownloaded


        } catch (e: IOException) {
            Timber.tag(TAG).e("There was an error saving the file - ${e.stackTraceToString()}")
            return 0
        }
    }

    @Throws(IOException::class)
    fun unZipFile(zipFile: File, targetDirectory: File): Long {
        return ZipFile(zipFile).unzip(targetDirectory)
    }

    private fun ZipFile.unzip(
        unzipDir: File
    ): Long {
        var fileSizeDownloaded: Long = 0
        val enum = entries()

        Timber.tag(TAG).i("Unzipping File")

        while (enum.hasMoreElements()) {
            val entry = enum.nextElement()
            val entryName = entry.name
            val fis = FileInputStream(this.name)
            ZipInputStream(fis).use { zis ->
                while (true) {
                    val nextEntry = zis.nextEntry ?: break
                    if (nextEntry.name == entryName) {
                        FileOutputStream(File(unzipDir, nextEntry.name)).use {
                            var c = zis.read()
                            while (c != -1) {
                                it.write(c)
                                c = zis.read()
                                fileSizeDownloaded += c.toLong()
                            }
                            zis.closeEntry()
                        }
                    }

                    Timber.tag(TAG).i("fileSizeDownloaded $fileSizeDownloaded")
                }
            }
        }
        return fileSizeDownloaded
    }

    private suspend fun startForegroundService() {
        setForeground(
            ForegroundInfo(
                Random.nextInt(),
                NotificationCompat.Builder(context, CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setContentText("Downloading...")
                    .setContentTitle("Download in progress")
                    .build()
            )
        )
    }

    private fun dataOnFailure(error : String): Data {
        return workDataOf(
            ERROR_MSG to error,
        )
    }

    private fun dataOnSuccess(uri : String): Data {
        return workDataOf(
            MEDIA_URI to uri,
        )
    }

    companion object {
        const val WORK_NAME = "DownloadWork"
        const val TAG = "DownloadWorkTag"
        const val CHANNEL_ID = "DownloadChannel"
        private const val CHANNEL_NAME = "Media Downloader"
        private const val CHANNEL_DESCRIPTION = "Worker that downloads media files"

        private const val RESOURCE_DIRECTORY = "media"

        @RequiresApi(Build.VERSION_CODES.O)
        fun getNotificationChannel(): NotificationChannel {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            )

            channel.description = CHANNEL_DESCRIPTION
            return channel
        }
        fun createWorkRequest(
        ): OneTimeWorkRequest {
            val constraints = Constraints.Builder().setRequiresStorageNotLow(true)
                .setRequiredNetworkType(NetworkType.CONNECTED).build()
            return OneTimeWorkRequestBuilder<DownloadWorker>().addTag(TAG)
                .setConstraints(constraints)
                .build()
        }
    }
}