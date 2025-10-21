package eu.peernetwork.blog.data.api

interface MultipartApi {
    suspend fun upload(token: String, paths: List<String>): String
}
