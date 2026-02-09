package eu.peernetwork.blog.data.repository

import eu.peernetwork.blog.data.api.MultipartApi
import eu.peernetwork.blog.domain.repository.MultipartRepository
import javax.inject.Inject

class MultipartRepositoryDelegate @Inject constructor(
    private val api: MultipartApi
) : MultipartRepository {
    override suspend fun upload(token: String, paths: List<String>): String {
        return api.upload(token, paths)
    }
}
