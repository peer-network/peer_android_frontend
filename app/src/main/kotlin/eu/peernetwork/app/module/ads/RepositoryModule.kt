package eu.peernetwork.app.module.ads

import dagger.Binds
import dagger.Module
import eu.peernetwork.ads.data.repository.AdvertiserRepositoryDelegate
import eu.peernetwork.ads.domain.repository.AdvertiserRepository

@Module
interface RepositoryModule {
    @Binds
    fun bindAdvertiserRepository(delegate: AdvertiserRepositoryDelegate): AdvertiserRepository
}
