package eu.peernetwork.blog.domain.repository

import eu.peernetwork.blog.domain.model.Content
import eu.peernetwork.blog.domain.model.Draft

interface ContentMultipartRepository {
    suspend fun upload(file: Draft): Content
}