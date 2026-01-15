package eu.peernetwork.social.data.interactor

import eu.peernetwork.core.common.paging.Page
import eu.peernetwork.core.common.paging.Pageable
import eu.peernetwork.social.domain.interactor.SearchInteractor
import eu.peernetwork.social.domain.model.Member
import eu.peernetwork.user.domain.model.Status
import eu.peernetwork.user.domain.repository.SearchRepository
import javax.inject.Inject

class SearchInteractorDelegate @Inject constructor(
    private val repository: SearchRepository,
) : SearchInteractor {
    override suspend fun findMember(
        username: String,
        pageable: Pageable
    ): Page<Member> {
        val response = repository.filterByUsername(username, pageable)
        return Page(
            count = response.count,
            offset = pageable.offset,
            items = response.items.map {
                Member(
                    id = it.id,
                    slug = it.slug.toString(),
                    username = it.username,
                    imageUrl = it.imageUrl,
                    isFollowed = false,
                    isFollowing = false,
                    isAccessible = it.isAccessible,
                    status = when(it.status) {
                        Status.VISIBLE -> eu.peernetwork.social.domain.model.Status.VISIBLE
                        Status.HIDDEN -> eu.peernetwork.social.domain.model.Status.HIDDEN
                        Status.ILLEGAL -> eu.peernetwork.social.domain.model.Status.ILLEGAL
                    }
                )
            }
        )
    }
}
