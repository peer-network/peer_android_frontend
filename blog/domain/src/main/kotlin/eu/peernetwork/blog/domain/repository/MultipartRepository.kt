package eu.peernetwork.blog.domain.repository

import java.io.File

interface MultipartRepository {
    suspend fun upload(file: File): String
}