package eu.peernetwork.blog.domain.interactor

import eu.peernetwork.blog.domain.model.Content
import eu.peernetwork.blog.domain.model.Draft

interface ContentInteractor {
    suspend fun create(draft: Draft): Content
}
