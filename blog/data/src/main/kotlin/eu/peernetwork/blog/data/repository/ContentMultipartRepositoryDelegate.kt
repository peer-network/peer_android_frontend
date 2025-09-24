package eu.peernetwork.blog.data.repository

import eu.peernetwork.blog.data.api.ContentMultipartApi
import eu.peernetwork.blog.domain.model.Content
import eu.peernetwork.blog.domain.model.Draft
import eu.peernetwork.blog.domain.repository.ContentMultipartRepository
import javax.inject.Inject

class ContentMultipartRepositoryDelegate @Inject constructor(
    private val api: ContentMultipartApi
) : ContentMultipartRepository {
    override suspend fun upload(file: Draft): Content {
        return api.upload(file)
    }
}