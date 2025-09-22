package eu.peernetwork.media.ui.usecase

import android.content.ContentUris
import android.content.Context
import android.os.Bundle
import android.provider.MediaStore
import eu.peernetwork.core.common.provider.Dispatcher
import eu.peernetwork.core.common.usecase.ParameterizedSuspendableUseCase
import eu.peernetwork.media.core.model.UiFile
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AudioUsecase @Inject constructor(
    private val context: Context,
    private val dispatcher: Dispatcher
): ParameterizedSuspendableUseCase<String?, List<UiFile>> {
    override suspend fun invoke(param: String?): List<UiFile> = withContext(dispatcher.io) {
        val files = mutableListOf<UiFile>()
        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.DISPLAY_NAME,
            MediaStore.Audio.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Audio.Media.BUCKET_ID
        )
        val uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val mimeSelection = "${MediaStore.Audio.Media.MIME_TYPE} IN (?, ?, ?, ?, ?, ?, ?, ?)"
        val mimeArgs = arrayOf(
            "audio/aac",
            "audio/basic",
            "audio/flac",
            "audio/mp4",
            "audio/mpeg",
            "audio/ogg",
            "audio/x-aiff",
            "audio/x-wav"
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
            "${MediaStore.Audio.Media.DATE_ADDED} DESC"
        )
        cursor?.use {
            val idColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
            val nameColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)
            while (it.moveToNext()) {
                val audioUri = ContentUris.withAppendedId(uri, it.getLong(idColumn))
                val name = it.getString(nameColumn)
                val props = Bundle().apply {
                    putString("name", name)
                }
                files.add(UiFile(uri = audioUri, props = props))
            }
        }
        files
    }
}