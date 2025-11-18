package eu.peernetwork.app.module.ads

import dagger.Binds
import dagger.Module
import eu.peernetwork.ads.data.repository.AdvertiserRepositoryDelegate
import eu.peernetwork.ads.data.repository.ContentRepositoryDelegate
import eu.peernetwork.ads.domain.repository.AdvertiserRepository
import eu.peernetwork.ads.domain.repository.ContentRepository

@Module
interface RepositoryModule {
    @Binds
    fun bindAdvertiserRepository(delegate: AdvertiserRepositoryDelegate): AdvertiserRepository

    @Binds
    fun bindContentRepository(delegate: ContentRepositoryDelegate): ContentRepository
}
