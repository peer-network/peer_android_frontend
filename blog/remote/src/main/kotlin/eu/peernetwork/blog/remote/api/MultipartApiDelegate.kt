package eu.peernetwork.blog.remote.api

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import eu.peernetwork.blog.data.api.MultipartApi
import eu.peernetwork.blog.remote.helper.RequestHelper
import eu.peernetwork.core.remote.exception.NetworkException
import eu.peernetwork.core.remote.exception.UndefinedResponseException
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject
import javax.inject.Named

class MultipartApiDelegate @Inject constructor(
    private val gson: Gson,
    private val helper: RequestHelper,
    @Named("baseUrl") private val url: String,
    private val rest: OkHttpClient
    ) : MultipartApi {
    override suspend fun upload(token: String, paths: List<String>): String {
        val files = paths.map { File(it) }
        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("eligibilityToken", token)
        files.forEach { file ->
            requestBody.addFormDataPart(
                "file",
                file.name,
                file.asRequestBody(helper.getType(file).toMediaType())
            )
        }
        val request = Request.Builder()
            .url("$url/upload-post")
            .post(requestBody.build())
            .build()
        val response = rest.newCall(request).execute()
        if (!response.isSuccessful) {
            throw NetworkException(response.message)
        }
        return response.body?.string()?.let {
            val model = gson.fromJson(it, ResponseModel::class.java)
            model.content ?: throw NetworkException(model.code)
        } ?: throw UndefinedResponseException()
    }

    data class ResponseModel(
        @SerializedName("ResponseCode") val code: String,
        @SerializedName("uploadedFiles") val content: String?
    )
}