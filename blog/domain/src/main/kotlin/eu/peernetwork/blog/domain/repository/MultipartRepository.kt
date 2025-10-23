package eu.peernetwork.blog.domain.repository

interface MultipartRepository {
    suspend fun upload(token: String, paths: List<String>): String
}