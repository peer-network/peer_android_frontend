package eu.peernetwork.user.remote.usecase

import android.util.Base64
import eu.peernetwork.core.common.exception.BusinessException
import eu.peernetwork.core.common.usecase.ParameterizedBlockingUseCase
import org.json.JSONObject
import javax.inject.Inject

class JwtExpiryUsecase @Inject constructor() : ParameterizedBlockingUseCase<String, Long> {
    override fun invoke(param: String): Long {
        val parts = param.split(".")
        if (parts.size != 3) {
            throw BusinessException("authorization", "Invalid JWT format")
        }
        val payload = parts[1]
        val decodedBytes = Base64.decode(payload, Base64.URL_SAFE or Base64.NO_PADDING or Base64.NO_WRAP)
        val decodedString = String(decodedBytes, charset("UTF-8"))
        return JSONObject(decodedString).getLong("exp")
    }
}
