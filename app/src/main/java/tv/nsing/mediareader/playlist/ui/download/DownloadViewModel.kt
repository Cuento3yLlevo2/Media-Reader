package tv.nsing.mediareader.playlist.ui.download

import android.app.Application
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.Data
import androidx.work.WorkInfo
import androidx.work.WorkManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import tv.nsing.mediareader.playlist.domain.GetPlaylistFromJsonUseCase
import tv.nsing.mediareader.playlist.ui.download.DownloadUiState
import tv.nsing.mediareader.playlist.ui.playlist.PlaylistUiState
import tv.nsing.mediareader.playlist.worker.DownloadObserver
import tv.nsing.mediareader.playlist.worker.DownloadWorker
import tv.nsing.mediareader.playlist.worker.WorkerKeys
import tv.nsing.mediareader.playlist.worker.WorkerKeys.DOWNLOAD_STATE
import javax.inject.Inject

@HiltViewModel
class DownloadViewModel @Inject constructor(
    private val getPlaylistFromJsonUseCase: GetPlaylistFromJsonUseCase,
    private val workManager: WorkManager
) : ViewModel() {

    private val _uiState = MutableLiveData<DownloadUiState>()
    val uiState: LiveData<DownloadUiState> = _uiState

    private var _downloadData: LiveData<WorkInfo>? = null
    private val downloadObserver = DownloadObserver(
        onDownloadFinished = { outputData ->
            onDownloadFinished(outputData)
        },
        onDownloadRunning = { progress -> onDownloadRunning(progress) }
    )

    private val _downloadState = MutableLiveData<String>()
    val downloadState: LiveData<String> = _downloadState


    init {
        _uiState.value = DownloadUiState.Init
    }

    fun downloadZipFile() {
        viewModelScope.launch {
            _uiState.value = DownloadUiState.Downloading
            val downloadRequest = DownloadWorker.createWorkRequest()
            _downloadData = workManager.getWorkInfoByIdLiveData(downloadRequest.id)
            Handler(Looper.getMainLooper()).post {
                _downloadData?.observeForever(
                    downloadObserver
                )
            }
            workManager.enqueue(downloadRequest)
        }
    }

    private fun onDownloadFinished(outputData: Data) {
        viewModelScope.launch {
            val error = outputData.getString(WorkerKeys.ERROR_MSG)
            if (error != null) {
                _uiState.value = DownloadUiState.Error(error)
            } else {
                _uiState.value = DownloadUiState.Success
            }
        }
    }

    private fun onDownloadRunning(progress: Data) {
        viewModelScope.launch {
            _downloadState.value = progress.getString(DOWNLOAD_STATE)
        }
    }

    override fun onCleared() {
        Handler(Looper.getMainLooper()).post {
            _downloadData?.removeObserver(downloadObserver)
        }
        super.onCleared()
    }
}