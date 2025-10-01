package eu.peernetwork.blog.data.api

import eu.peernetwork.blog.domain.model.Content
import java.io.File

interface MultipartApi {
    suspend fun upload(file: File): Content
}