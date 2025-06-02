package eu.peernetwork.media.ui.usecase

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import eu.peernetwork.core.common.provider.Dispatcher
import eu.peernetwork.core.common.usecase.SuspendableUseCase
import eu.peernetwork.media.core.model.UiDirectory
import kotlinx.coroutines.withContext
import javax.inject.Inject

class VideoDirectoryUsecase @Inject constructor(
    private val context: Context,
    private val dispatcher: Dispatcher
) : SuspendableUseCase<Set<UiDirectory>> {
    override suspend fun invoke(): Set<UiDirectory> = withContext(dispatcher.io) {
        val projection = arrayOf(
            MediaStore.Video.Media._ID,
            MediaStore.Video.Media.DATA,
            MediaStore.Images.Media.RELATIVE_PATH,
            MediaStore.Video.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Video.Media.BUCKET_ID
        )
        val uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
        val folders = mutableMapOf<String, Model>()
        context.contentResolver.query(uri, projection, null, null, null)?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID)
            val dataColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)
            val bucketColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.BUCKET_DISPLAY_NAME)
            val pathColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.RELATIVE_PATH)
            while (cursor.moveToNext()) {
                val key = cursor.getString(bucketColumn)
                if (!folders.containsKey(key)) {
                    folders[key] = Model(
                        cursor.getString(pathColumn),
                        cursor.getString(dataColumn),
                        Uri.withAppendedPath(uri, cursor.getLong(idColumn).toString())
                    )
                }
            }
        }
        folders.map { UiDirectory(it.key, it.value.path, it.value.uri, it.value.thumbnail) }.toSet()
    }

    data class Model(
        val path: String,
        val thumbnail: String,
        val uri: Uri,
    )
}
