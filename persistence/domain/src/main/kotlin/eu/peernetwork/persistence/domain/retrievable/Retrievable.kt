package eu.peernetwork.persistence.domain.retrievable

interface Retrievable<T> {
    operator fun invoke(key: String): T?
}
