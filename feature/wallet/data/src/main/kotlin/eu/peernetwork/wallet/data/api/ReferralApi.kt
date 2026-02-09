package eu.peernetwork.wallet.data.api

interface ReferralApi {
    suspend fun get(): String?
}
