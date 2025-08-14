package eu.peernetwork.app.service

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import eu.peernetwork.app.PeerApplication
import eu.peernetwork.app.usecase.NotificationUsecase
import eu.peernetwork.app.usecase.SubscriptionUsecase
import eu.peernetwork.core.common.provider.Dispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import javax.inject.Inject

class MessagingService : FirebaseMessagingService() {
    @Inject
    internal lateinit var dispatcher: Dispatcher

    @Inject
    internal lateinit var notification: NotificationUsecase

    @Inject
    internal lateinit var subscription: SubscriptionUsecase

    private val scope: CoroutineScope by lazy {
        CoroutineScope(SupervisorJob() + dispatcher.io)
    }

    override fun onCreate() {
        super.onCreate()
        (application as PeerApplication).injector.inject(this)
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        scope.launch {
            subscription(token)
        }
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        notification(message)
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }
}
