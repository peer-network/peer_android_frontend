package eu.peernetwork.app.module.social

import dagger.Binds
import dagger.Module
import eu.peernetwork.social.data.repository.FollowRepositoryDelegate
import eu.peernetwork.social.domain.repository.FollowRepository

@Module
interface RepositoryModule {
    @Binds
    fun bindsFollowRepository(delegate: FollowRepositoryDelegate): FollowRepository
}
