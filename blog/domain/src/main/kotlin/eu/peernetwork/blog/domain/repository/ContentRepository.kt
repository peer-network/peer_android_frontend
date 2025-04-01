package eu.peernetwork.blog.domain.repository

import eu.peernetwork.blog.domain.model.Filter
import eu.peernetwork.blog.domain.model.Media
import eu.peernetwork.core.common.model.Pageable

interface MediaRepository {
    suspend fun get(id: String): Media

    suspend fun getAll(filter: Filter = Filter(), page: Pageable): List<Media>

    suspend fun create()
}
