package eu.peernetwork.social.data.repository

import eu.peernetwork.core.common.paging.Page
import eu.peernetwork.core.common.paging.Pageable
import eu.peernetwork.social.data.api.SearchApi
import eu.peernetwork.social.domain.model.Post
import eu.peernetwork.social.domain.model.Tag
import eu.peernetwork.social.domain.repository.SearchRepository
import javax.inject.Inject

class SearchRepositoryDelegate @Inject constructor(
    private val api: SearchApi
) : SearchRepository {
    override suspend fun findAllTags(
        tag: String,
        pageable: Pageable
    ): Page<Tag> {
        return api.findAllTags(tag, pageable)
    }

    override suspend fun findPostsByTitle(title: String, pageable: Pageable): Page<Post> {
        return api.findPostsByTitle(title, pageable)
    }
}
