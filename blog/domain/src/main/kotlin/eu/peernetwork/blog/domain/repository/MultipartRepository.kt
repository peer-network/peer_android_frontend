package eu.peernetwork.blog.domain.repository

import eu.peernetwork.blog.domain.model.Content
import java.io.File

interface MultipartRepository {
    suspend fun upload(file: File): Content
}