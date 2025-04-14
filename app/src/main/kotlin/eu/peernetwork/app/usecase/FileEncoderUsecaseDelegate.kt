package eu.peernetwork.app.usecase

import android.util.Base64
import eu.peernetwork.core.common.usecase.FileEncoderUsecase
import java.io.ByteArrayOutputStream
import javax.inject.Inject

class FileEncoderUsecaseDelegate @Inject constructor() : FileEncoderUsecase {
    override fun invoke(param: FileEncoderUsecase.Parameter): String {
        try {
            val byteArrayOutputStream = ByteArrayOutputStream()
            val buffer = ByteArray(1024)
            var bytesRead: Int
            while (param.content.read(buffer).also { bytesRead = it } != -1) {
                byteArrayOutputStream.write(buffer, 0, bytesRead)
            }
            val base64b = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT)
            return "data:${param.type};base64,$base64b"
        } finally {
            param.content.close()
        }
    }
}
