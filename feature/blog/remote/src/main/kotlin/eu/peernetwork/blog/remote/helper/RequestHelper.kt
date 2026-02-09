package eu.peernetwork.blog.remote.helper

import android.content.Context
import android.net.Uri
import android.webkit.MimeTypeMap
import java.io.File
import javax.inject.Inject
import kotlin.io.extension

interface RequestHelper {
    fun getType(file: File): String

    class Delegate @Inject constructor(private val context: Context) : RequestHelper {
        override fun getType(file: File): String {
            val uri = Uri.fromFile(file)
            context.contentResolver.getType(uri)?.let { return it }
            val extension = file.extension.lowercase()
            return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
                ?: "application/octet-stream"
        }
    }
}
