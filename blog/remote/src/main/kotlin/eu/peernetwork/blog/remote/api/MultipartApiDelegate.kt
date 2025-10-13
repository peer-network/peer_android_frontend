package eu.peernetwork.blog.remote.api

import eu.peernetwork.blog.data.api.MultipartApi
import eu.peernetwork.blog.domain.repository.EligibilityRepository
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject
import javax.inject.Named

class MultipartApiDelegate @Inject constructor(
    @Named("mediaUrl") private val url: String,
    private val rest: OkHttpClient,
    private val eligibilityRepository: EligibilityRepository
    ) : MultipartApi {
        val testurl = "https://peer-network.eu"
    override suspend fun upload(file: File): String {
        val token = eligibilityRepository.get() ?: eligibilityRepository.refresh()
        val mimeType = file.detectMediaType()
        val fileBody = file.asRequestBody(mimeType)
        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("eligibilityToken", token)
            .addFormDataPart("file", file.name, fileBody)
            .build()
        val request = Request.Builder()
            .url("$testurl/upload-post")
            .post(requestBody)
            .build()
        val response = rest.newCall(request).execute()
        if (!response.isSuccessful) {
            throw RuntimeException("Upload failed: ${response.code} - ${response.message}")
        }
        val bodyString = response.body?.string()
            ?: throw RuntimeException("Empty response body")

        return bodyString
    }

    private fun File.detectMediaType(): MediaType {
        val ext = extension.lowercase()
        val mimeType = when (ext) {
            "jpg", "jpeg" -> "image/jpeg"
            "png" -> "image/png"
            "gif" -> "image/gif"
            "webp" -> "image/webp"
            "mp3" -> "audio/mpeg"
            "wav" -> "audio/wav"
            "ogg" -> "audio/ogg"
            else -> "application/octet-stream"
        }
        return mimeType.toMediaType()
    }
}