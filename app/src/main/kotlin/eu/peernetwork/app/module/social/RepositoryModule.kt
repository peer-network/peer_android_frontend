package eu.peernetwork.app.module.social

import dagger.Binds
import dagger.Module
import eu.peernetwork.social.data.repository.ModerationRepositoryDelegate
import eu.peernetwork.social.data.repository.FollowRepositoryDelegate
import eu.peernetwork.social.data.repository.InviteRepositoryDelegate
import eu.peernetwork.social.data.repository.ReferralRepositoryDelegate
import eu.peernetwork.social.data.repository.SearchRepositoryDelegate
import eu.peernetwork.social.domain.repository.ModerationRepository
import eu.peernetwork.social.domain.repository.FollowRepository
import eu.peernetwork.social.domain.repository.InviteRepository
import eu.peernetwork.social.domain.repository.ReferralRepository
import eu.peernetwork.social.domain.repository.SearchRepository

@Module
interface RepositoryModule {
    @Binds
    fun bindsFollowRepository(delegate: FollowRepositoryDelegate): FollowRepository

    @Binds
    fun bindSearchRepository(delegate: SearchRepositoryDelegate): SearchRepository

    @Binds
    fun bindReferralRepository(delegate: ReferralRepositoryDelegate): ReferralRepository

    @Binds
    fun bindBlockRepository(delegate: ModerationRepositoryDelegate): ModerationRepository

    @Binds
    fun bindInviteRepository(delegate: InviteRepositoryDelegate): InviteRepository
}
