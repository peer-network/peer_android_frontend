package eu.peernetwork.blog.domain.repository

import eu.peernetwork.blog.domain.model.Filter
import eu.peernetwork.blog.domain.model.Content
import eu.peernetwork.blog.domain.model.Draft
import eu.peernetwork.core.common.model.Page
import eu.peernetwork.core.common.model.Pageable

interface ContentRepository {
    suspend fun get(id: String): Content

    suspend fun getAll(filter: Filter = Filter(), page: Pageable): Page<Content>

    suspend fun create(draft: Draft): Content
}
