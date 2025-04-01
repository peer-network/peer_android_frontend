package eu.peernetwork.blog.data.api

import eu.peernetwork.blog.domain.model.Content
import eu.peernetwork.blog.domain.model.Draft
import eu.peernetwork.blog.domain.model.Filter
import eu.peernetwork.core.common.model.Pageable

interface ContentApi {
    suspend fun get(filter: Filter = Filter(), page: Pageable): List<Content>

    suspend fun create(draft: Draft, media: List<String>, cover: String? = null): Content
}
