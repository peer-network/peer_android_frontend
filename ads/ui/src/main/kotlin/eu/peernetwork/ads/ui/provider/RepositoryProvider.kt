package eu.peernetwork.ads.ui.provider

import eu.peernetwork.ads.domain.repository.AdvertiserRepository

interface RepositoryProvider {
    fun advertiserRepository(): AdvertiserRepository
}
