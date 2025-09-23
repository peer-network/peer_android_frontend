package eu.peernetwork.app.usecase

import android.os.Bundle
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import eu.peernetwork.core.common.usecase.ParameterizedSuspendableUseCase
import javax.inject.Inject

class OnboardingUpdateUsecase @Inject constructor() : ParameterizedSuspendableUseCase<Boolean, Unit> {
    override suspend fun invoke(param: Boolean) {
        Firebase.analytics.logEvent("device_model_logged", Bundle().apply {
            putBoolean("onboarding", param)
        })
    }
}
