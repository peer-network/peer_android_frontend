package eu.peernetwork.media.ui.usecase

import android.content.ContentUris
import android.content.Context
import android.provider.MediaStore
import eu.peernetwork.core.common.provider.Dispatcher
import eu.peernetwork.core.common.usecase.ParameterizedSuspendableUseCase
import eu.peernetwork.media.core.model.UiFile
import kotlinx.coroutines.withContext
import javax.inject.Inject

class VideoUsecase @Inject constructor(
    private val context: Context,
    private val dispatcher: Dispatcher
) : ParameterizedSuspendableUseCase<String?, List<UiFile>> {
    override suspend fun invoke(param: String?): List<UiFile> = withContext(dispatcher.io) {
        val files = mutableListOf<UiFile>()
        val projection = arrayOf(
            MediaStore.Video.Media._ID,
            MediaStore.Video.Media.DATA,
            MediaStore.Video.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Video.Media.BUCKET_ID
        )
        val uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
        val mimeSelection = "${MediaStore.Video.Media.MIME_TYPE} IN (?, ?, ?, ?)"
        val mimeArgs = arrayOf(
            "video/mp4",
            "video/quicktime",
            "video/x-matroska",
            "video/webm"
        )
        val selection: String
        val selectionArgs: Array<String>
        if (param != null) {
            selection = "($mimeSelection) AND (${MediaStore.Video.Media.DATA} LIKE " +
                    "? OR ${MediaStore.Video.Media.RELATIVE_PATH} LIKE ?)"
            selectionArgs = mimeArgs + arrayOf("%$param%", "%$param%")
        } else {
            selection = mimeSelection
            selectionArgs = mimeArgs
        }
        val cursor = context.contentResolver.query(
            uri,
            projection,
            selection,
            selectionArgs,
            "${MediaStore.Video.Media.DATE_ADDED} DESC"
        )
        cursor?.use {
            val idColumn = it.getColumnIndexOrThrow(MediaStore.Video.Media._ID)
            val dataColumn = it.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)
            while (it.moveToNext()) {
                val imageUri = ContentUris.withAppendedId(uri, it.getLong(idColumn))
                files.add(UiFile(imageUri, cursor.getString(dataColumn)))
            }
        }
        files
    }
}
