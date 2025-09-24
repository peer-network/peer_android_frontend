package eu.peernetwork.blog.domain.interactor

import eu.peernetwork.blog.domain.model.Content
import eu.peernetwork.blog.domain.model.Draft

interface ContentMultipartInteractor {
    suspend fun upload(file: Draft): Content
}
