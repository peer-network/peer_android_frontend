package eu.peernetwork.app.module.social

import dagger.Binds
import dagger.Module
import eu.peernetwork.social.data.repository.FollowRepositoryDelegate
import eu.peernetwork.social.data.repository.SearchRepositoryDelegate
import eu.peernetwork.social.domain.repository.FollowRepository
import eu.peernetwork.social.domain.repository.SearchRepository

@Module
interface RepositoryModule {
    @Binds
    fun bindsFollowRepository(delegate: FollowRepositoryDelegate): FollowRepository

    @Binds
    fun bindSearchRepository(delegate: SearchRepositoryDelegate): SearchRepository
}
