package eu.peernetwork.blog.data.repository

import eu.peernetwork.blog.data.api.ContentApi
import eu.peernetwork.blog.domain.model.Content
import eu.peernetwork.blog.domain.model.Draft
import eu.peernetwork.blog.domain.model.Filter
import eu.peernetwork.blog.domain.repository.ContentRepository
import eu.peernetwork.core.common.model.Page
import eu.peernetwork.core.common.model.Pageable
import javax.inject.Inject

class ContentRepositoryDelegate @Inject constructor(
    private val api: ContentApi
) : ContentRepository {
    override suspend fun get(id: String): Content {
        return api.get(Filter(postId = id), Pageable(0, 1)).items.first()
    }

    override suspend fun getAll(filter: Filter, page: Pageable): Page<Content> {
        return api.get(filter, page)
    }

    override suspend fun create(draft: Draft): Content {
        return api.create(draft)
    }
}
