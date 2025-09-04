package eu.peernetwork.core.common.interactor

interface ResourceInteractor {
    fun getBaseUrl(): String

    fun string(key: String): String
}