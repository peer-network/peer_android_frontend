package eu.peernetwork.app.usecase

import com.google.firebase.firestore.FirebaseFirestore
import eu.peernetwork.core.common.usecase.ParameterizedSuspendableUseCase
import javax.inject.Inject

class SubscriptionUsecase @Inject constructor(
    private val store: FirebaseFirestore
) : ParameterizedSuspendableUseCase<String, Unit> {
    override suspend fun invoke(param: String) {
    }
}
