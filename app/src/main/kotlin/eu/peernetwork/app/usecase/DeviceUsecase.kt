package eu.peernetwork.app.usecase

import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import eu.peernetwork.core.common.usecase.ParameterizedSuspendableUseCase
import javax.inject.Inject

class DeviceUsecase @Inject constructor() : ParameterizedSuspendableUseCase<String, Unit> {
    override suspend fun invoke(param: String) {
        Firebase.analytics.setUserId(param)
    }
}
