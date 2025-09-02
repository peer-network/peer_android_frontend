package eu.peernetwork.social.data.repository

import eu.peernetwork.core.common.paging.Page
import eu.peernetwork.core.common.paging.Pageable
import eu.peernetwork.social.data.api.FollowApi
import eu.peernetwork.social.domain.model.Member
import eu.peernetwork.social.domain.repository.FollowRepository
import javax.inject.Inject

class FollowRepositoryDelegate @Inject constructor(
    private val api: FollowApi
) : FollowRepository {
    override suspend fun follow(id: String): Boolean {
        return api.follow(id)
    }

    override suspend fun followers(
        id: String,
        pageable: Pageable
    ): Page<Member> {
        return api.followers(id, pageable)
    }

    override suspend fun following(
        id: String,
        pageable: Pageable
    ): Page<Member> {
        return api.following(id, pageable)
    }

    override suspend fun friends(
        pageable: Pageable
    ): Page<Member> {
        return api.friends(pageable)
    }
}
