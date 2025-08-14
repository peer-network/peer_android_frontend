package eu.peernetwork.app.interceptor

import com.google.firebase.functions.FirebaseFunctions
import eu.peernetwork.core.common.interactor.NotificationInteractor
import eu.peernetwork.user.domain.interactor.AuthenticationInteractor
import javax.inject.Inject

class NotificationInteractorDelegate @Inject constructor(
    private val functions: FirebaseFunctions,
    private val interactor: AuthenticationInteractor
) : NotificationInteractor {
    override suspend fun send(
        to: String,
        action: String,
        message: String
    ) {
        val user = interactor.getCurrentAccount(false)
        functions.getHttpsCallable("sendUserNotification").call(
            mapOf(
                "action" to action,
                "targetUid" to to,
                "username" to user.username,
                "postTitle" to message
            )
        )
    }
}
