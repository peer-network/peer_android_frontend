package eu.peernetwork.app.service

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import eu.peernetwork.app.interceptor.SubscriptionInteractor
import eu.peernetwork.persistence.domain.publishable.PublishableString
import eu.peernetwork.persistence.domain.retrievable.RetrievableString
import javax.inject.Inject

class SubscriptionService @Inject constructor(
    private val store: FirebaseFirestore,
    private val publishableString: PublishableString,
    private val retrievableString: RetrievableString
) : SubscriptionInteractor {
    private val tag = SubscriptionService::class.java.name
    override suspend fun setToken(token: String) {
        publishableString(tag, token)
    }

    override suspend fun subscribe(uuid: String) {
        retrievableString(tag)?.let {
            publishableString(TOKEN, uuid)
            store.collection(COLLECTION)
                .document(uuid)
                .set(mapOf(TOKEN to FieldValue.arrayUnion(it)))
        }
    }

    override suspend fun unSubscribe() {
        val token = retrievableString(tag)
        retrievableString(TOKEN)?.let {
            store.collection(COLLECTION)
                .document(it)
                .set(mapOf(TOKEN to FieldValue.arrayRemove(token)))
        }
        publishableString(TOKEN, null)
    }

    private companion object {
        const val TOKEN = "fcmTokens"
        const val COLLECTION = "users"
    }
}
