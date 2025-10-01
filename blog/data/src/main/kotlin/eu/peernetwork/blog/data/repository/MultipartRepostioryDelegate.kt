package eu.peernetwork.blog.data.repository

import eu.peernetwork.blog.data.api.MultipartApi
import eu.peernetwork.blog.domain.model.Content
import eu.peernetwork.blog.domain.repository.MultipartRepository
import java.io.File
import javax.inject.Inject

class MultipartRepostioryDelegate @Inject constructor(
    private val api: MultipartApi
) : MultipartRepository {
    override suspend fun upload(file: File): Content {
        return api.upload(file)
    }
}