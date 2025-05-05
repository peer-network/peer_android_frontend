package eu.peernetwork.social.data.api

import eu.peernetwork.core.common.model.Page
import eu.peernetwork.core.common.model.Pageable
import eu.peernetwork.social.domain.model.Post
import eu.peernetwork.social.domain.model.Tag

interface SearchApi {
    suspend fun findAllTags(tag: String, pageable: Pageable): Page<Tag>

    suspend fun findPostsByTitle(title: String, pageable: Pageable): Page<Post>
}
