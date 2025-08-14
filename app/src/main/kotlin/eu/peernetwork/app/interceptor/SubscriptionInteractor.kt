package eu.peernetwork.app.interceptor

interface SubscriptionInteractor {
    suspend fun setToken(token: String)

    suspend fun subscribe(uuid: String)

    suspend fun unSubscribe()
}
