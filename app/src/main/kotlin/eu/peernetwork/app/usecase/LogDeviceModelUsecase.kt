package eu.peernetwork.app.usecase

import android.os.Build
import android.os.Bundle
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import eu.peernetwork.core.common.usecase.SuspendableUseCase
import javax.inject.Inject


class LogDeviceModelUsecase @Inject constructor() : SuspendableUseCase<Unit> {
    override suspend fun invoke() {
        val model = Build.MODEL ?: "Unknown"
        Firebase.analytics.setUserProperty("device_model", model)
        Firebase.analytics.logEvent("device_model_logged", Bundle().apply {
            putString("device_model", model)
        })
    }
}
