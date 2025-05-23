package eu.peernetwork.core.common.interactor

interface UrlInteractor {
    fun get(): String

    fun invite(): String

    fun set(url: String)

    fun invite(url: String)
}
