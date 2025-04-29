package eu.peernetwork.social.data.interactor

import eu.peernetwork.core.common.model.Page
import eu.peernetwork.core.common.model.Pageable
import eu.peernetwork.social.domain.interactor.SearchInteractor
import eu.peernetwork.social.domain.model.Content
import eu.peernetwork.social.domain.model.Member
import eu.peernetwork.user.domain.repository.SearchRepository
import javax.inject.Inject

class SearchInteractorDelegate @Inject constructor(
    private val repository: SearchRepository
) : SearchInteractor {
    override suspend fun tag(
        username: String,
        pageable: Pageable
    ): Page<Content> {
        TODO("Not yet implemented")
    }

    override suspend fun title(
        username: String,
        pageable: Pageable
    ): Page<Content> {
        TODO("Not yet implemented")
    }

    override suspend fun user(
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
                    username = it.username,
                    imageUrl = it.imageUrl
                )
            }
        )
    }
}
