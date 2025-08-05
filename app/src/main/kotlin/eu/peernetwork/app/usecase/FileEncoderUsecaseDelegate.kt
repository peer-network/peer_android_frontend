package eu.peernetwork.app.usecase

import android.util.Base64
import android.util.Base64OutputStream
import eu.peernetwork.core.common.usecase.FileEncoderUsecase
import java.io.ByteArrayOutputStream
import javax.inject.Inject

class FileEncoderUsecaseDelegate @Inject constructor() : FileEncoderUsecase {
    override fun invoke(param: FileEncoderUsecase.Parameter): String {
        try {
            val output = ByteArrayOutputStream()
            val buffer = ByteArray(8192)
            val base64Output = Base64OutputStream(output, Base64.NO_WRAP)
            param.content.use { input ->
                base64Output.use { base64 ->
                    var bytesRead: Int
                    while (input.read(buffer).also { bytesRead = it } != -1) {
                        base64.write(buffer, 0, bytesRead)
                    }
                }
            }
            return "data:${param.type};base64,${output.toString(Charsets.UTF_8.name())}"
        } finally {
            param.content.close()
        }
    }
}
