package eu.peernetwork.persistence.domain.publishable

interface Publishable<T> {
    suspend operator fun invoke(key: String, value: T?)
}
