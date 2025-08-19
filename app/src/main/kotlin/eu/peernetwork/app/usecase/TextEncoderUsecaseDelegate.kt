package eu.peernetwork.app.usecase

import android.util.Base64
import eu.peernetwork.media.core.usecase.TextEncoderUsecase
import javax.inject.Inject

class TextEncoderUsecaseDelegate @Inject constructor() : TextEncoderUsecase {
    override fun invoke(param: String): String {
        val encoded = Base64.encodeToString(
            param.toByteArray(Charsets.UTF_8),
            Base64.DEFAULT
        )
        return "data:text/plain;base64,$encoded"
    }
}
