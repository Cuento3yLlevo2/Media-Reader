package tv.nsing.mediareader.playlist.worker

import androidx.lifecycle.Observer
import androidx.work.Data
import androidx.work.WorkInfo

class DownloadObserver(
    val onDownloadFinished : (outputData: Data) -> Unit,
    val onDownloadRunning : (progress: Data) -> Unit,
): Observer<WorkInfo> {
    override fun onChanged(value: WorkInfo) {
        when (value.state) {
            WorkInfo.State.FAILED -> {
                if (value.state.isFinished) {
                    onDownloadFinished(value.outputData)
                }
            }
            WorkInfo.State.ENQUEUED -> {}
            WorkInfo.State.RUNNING -> onDownloadRunning(value.progress)
            WorkInfo.State.SUCCEEDED -> {
                if (value.state.isFinished) {
                    onDownloadFinished(value.outputData)
                }
            }
            WorkInfo.State.BLOCKED -> {}
            WorkInfo.State.CANCELLED -> {
                if (value.state.isFinished) {
                    onDownloadFinished(value.outputData)
                }
            }
        }
    }
}