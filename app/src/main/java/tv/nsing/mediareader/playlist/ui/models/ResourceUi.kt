package tv.nsing.mediareader.playlist.ui.models

import androidx.compose.ui.text.toLowerCase
import tv.nsing.mediareader.core.Constants
import java.io.File


data class ResourceUi(
    var id: String,
    var order: Int,
    var name: String,
    var duration: Int
)

fun ResourceUi.getMediaType(): PlatlistMediaType? {
    val videoFilenameExtensions = listOf("mp4", "wav")
    for (extension in videoFilenameExtensions) {
        if(name.endsWith(extension)) {
            return PlatlistMediaType.VIDEO_MEDIA_TYPE
        }
    }

    val imageFilenameExtensions = listOf("jpeg", "jpg", "png", "gif", "bmp")
    for (extension in imageFilenameExtensions) {
        if(name.endsWith(extension)) {
            return PlatlistMediaType.IMAGE_MEDIA_TYPE
        }
    }

    return null
}

fun ResourceUi.getFilePath(dataDir: String) : String {
    return dataDir + File.separator + Constants.RESOURCE_DIRECTORY_NAME + File.separator + name
}

enum class PlatlistMediaType {
    VIDEO_MEDIA_TYPE, IMAGE_MEDIA_TYPE
}




