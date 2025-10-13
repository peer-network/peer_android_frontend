package eu.peernetwork.blog.data.repository

import eu.peernetwork.blog.data.api.MultipartApi
import eu.peernetwork.blog.domain.repository.MultipartRepository
import java.io.File
import javax.inject.Inject

class MultipartRepositoryDelegate @Inject constructor(
    private val api: MultipartApi
) : MultipartRepository {
    override suspend fun upload(file: File): String {
        return api.upload(file)
    }
}