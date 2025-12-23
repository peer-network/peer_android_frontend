package eu.peernetwork.ads.ui.provider

import eu.peernetwork.ads.domain.repository.AdvertiserRepository
import eu.peernetwork.ads.domain.repository.ContentRepository

interface RepositoryProvider {
    fun advertiserRepository(): AdvertiserRepository

    fun adsContentRepository(): ContentRepository
}
