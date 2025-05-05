package eu.peernetwork.social.domain.repository

import eu.peernetwork.core.common.model.Page
import eu.peernetwork.core.common.model.Pageable
import eu.peernetwork.social.domain.model.Post
import eu.peernetwork.social.domain.model.Tag

interface SearchRepository {
    suspend fun findAllTags(tag: String, pageable: Pageable): Page<Tag>

    suspend fun findPostsByTitle(title: String, pageable: Pageable): Page<Post>
}
