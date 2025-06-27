package eu.peernetwork.media.ui.usecase

import android.content.ContentUris
import android.content.Context
import android.provider.MediaStore
import eu.peernetwork.core.common.provider.Dispatcher
import eu.peernetwork.core.common.usecase.ParameterizedSuspendableUseCase
import eu.peernetwork.media.core.model.UiFile
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PhotoUsecase @Inject constructor(
    private val context: Context,
    private val dispatcher: Dispatcher
) : ParameterizedSuspendableUseCase<String?, List<UiFile>> {
    override suspend fun invoke(param: String?): List<UiFile> = withContext(dispatcher.io) {
        val files = mutableListOf<UiFile>()
        val projection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Images.Media.BUCKET_ID
        )
        val uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val mimeSelection = "${MediaStore.Images.Media.MIME_TYPE} IN (?, ?, ?)"
        val mimeArgs = arrayOf("image/png", "image/jpeg", "image/jpg")
        val selection: String
        val selectionArgs: Array<String>
        if (param != null) {
            selection = "($mimeSelection) AND (${MediaStore.Images.Media.DATA} LIKE " +
                    "? OR ${MediaStore.Images.Media.RELATIVE_PATH} LIKE ?)"
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
            "${MediaStore.Images.Media.DATE_ADDED} DESC"
        )
        cursor?.use {
            val idColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            val dataColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            while (it.moveToNext()) {
                val imageUri = ContentUris.withAppendedId(uri, it.getLong(idColumn))
                val galleryPx = 300
                val path = cursor.getString(dataColumn)
                val thumbKey = "$path?gallery=${galleryPx}"
                files.add(UiFile(imageUri, thumbKey))
            }
        }
        files
    }
}
