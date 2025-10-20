package eu.peernetwork.blog.data.api

import eu.peernetwork.blog.domain.model.Content
import eu.peernetwork.blog.domain.model.Draft

interface ContentMultipartApi {
    suspend fun upload(file: Draft): Content
}