package eu.peernetwork.blog.remote.api

import com.google.gson.Gson
import eu.peernetwork.blog.data.api.ContentMultipartApi
import eu.peernetwork.blog.domain.model.Content
import eu.peernetwork.blog.domain.model.Draft
import eu.peernetwork.blog.remote.content.PostEligibilityQuery
import eu.peernetwork.core.remote.api.RequestClient
import eu.peernetwork.core.remote.extension.assertOrThrow
import eu.peernetwork.core.remote.extension.executeOrThrow
import eu.peernetwork.core.remote.extension.getOrThrow
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import javax.inject.Inject
import javax.inject.Named

class ContentMultipartApiDelegate @Inject constructor(
    private val gson: Gson,
    @Named("mediaUrl") private val url: String,
    private val client: RequestClient,
    private val rest: OkHttpClient
) : ContentMultipartApi {
    override suspend fun upload(file: Draft): Content {
        val eligibilityResponse = client().query(PostEligibilityQuery()).executeOrThrow()
        val eligibility = eligibilityResponse.getOrThrow().postEligibility
        eligibilityResponse.assertOrThrow(eligibility.status, eligibility.ResponseCode)
        val eligibilityToken = eligibility.eligibilityToken
        val filePath = when (val type = file.type) {
            is Draft.Type.Text -> type.files.firstOrNull()
            is Draft.Type.Image -> type.files.firstOrNull()
            is Draft.Type.Video -> type.files.firstOrNull()
            is Draft.Type.Audio -> type.files.firstOrNull()
        } ?: throw IllegalArgumentException("No file provided in Draft")
        val fileObj = java.io.File(filePath)
        if (!fileObj.exists()) throw IllegalArgumentException("File not found: $filePath")
        val mediaType = "application/octet-stream".toMediaType()
        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("eligibilityToken", eligibilityToken)
            .addFormDataPart(
                "file",
                fileObj.name,
                RequestBody.create(mediaType, fileObj)
            )
            .build()
        val request = Request.Builder()
            .url("$url/upload-post")
            .addHeader("Authorization", "Bearer $eligibilityToken")
            .post(requestBody)
            .build()
        val response = rest.newCall(request).execute()
        if (!response.isSuccessful) {
            throw RuntimeException("Upload failed: ${response.code} - ${response.message}")
        }
        val bodyString = response.body?.string()
            ?: throw RuntimeException("Empty response body")
        return gson.fromJson(bodyString, Content::class.java)
    }
}