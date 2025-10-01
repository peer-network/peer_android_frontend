package eu.peernetwork.blog.remote.api

import com.google.gson.Gson
import eu.peernetwork.blog.data.api.MultipartApi
import eu.peernetwork.blog.domain.model.Content
import eu.peernetwork.blog.domain.repository.EligibilityRepository
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject
import javax.inject.Named

class MultipartApiDelegate @Inject constructor(
    private val gson: Gson,
    @Named("mediaUrl") private val url: String,
    private val rest: OkHttpClient,
    private val eligibilityRepository: EligibilityRepository
    ) : MultipartApi {
    override suspend fun upload(file: File): Content {
        val token = eligibilityRepository.get() ?: eligibilityRepository.refresh()
        val mediaType = "application/octet-stream".toMediaType()
        val fileBody = file.asRequestBequestBody(mediaType)
        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("eligibilityToken", token)
            .addFormDataPart( name = "file", filename = file.name, body = fileBody )
            .build()
        val request = Request.Builder()
            .url("$url/upload-post")
            .addHeader("Authorization", "Bearer $token")
            .post(requestBody) .build()
        val response = rest.newCall(request).execute()
        if (!response.isSuccessful) {
            throw RuntimeException( "Upload failed: ${response.code} - ${response.message}" )
        }
        val bodyString = response.body?.string() ?: throw RuntimeException("Empty response body")
        return gson.fromJson(bodyString, Content::class.java)
    }

    private fun File.asRequestBequestBody(mediaType: MediaType): RequestBody { return RequestBody.create(mediaType, this) }
}