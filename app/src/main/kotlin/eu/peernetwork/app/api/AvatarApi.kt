package eu.peernetwork.app.api

import android.content.Context
import android.net.Uri
import android.util.Base64
import eu.peernetwork.user.data.api.SettingsApi
import eu.peernetwork.user.remote.api.AvatarSettingsApi
import java.io.ByteArrayOutputStream
import java.io.IOException
import javax.inject.Inject

class AvatarApi @Inject constructor(
    private val context: Context,
    private val api: AvatarSettingsApi,
) : SettingsApi.Updatable<Uri> {
    override suspend fun invoke(value: Uri) {
        convertToBase64(value)?.let {
            api(it)
        }
    }

    fun convertToBase64(param: Uri): String? {
        val inputStream = context.contentResolver.openInputStream(param)
        try {
            val byteArrayOutputStream = ByteArrayOutputStream()
            val buffer = ByteArray(1024)
            var bytesRead: Int
            while (inputStream?.read(buffer).also { bytesRead = it ?: -1 } != -1) {
                byteArrayOutputStream.write(buffer, 0, bytesRead)
            }
            val base64b = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT)
            return "data:image/jpeg;base64,$base64b"
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            inputStream?.close()
        }
        return null
    }
}
