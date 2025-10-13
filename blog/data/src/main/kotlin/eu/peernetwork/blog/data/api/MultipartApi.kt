package eu.peernetwork.blog.data.api

import java.io.File

interface MultipartApi {
    suspend fun upload(file: File): String
}