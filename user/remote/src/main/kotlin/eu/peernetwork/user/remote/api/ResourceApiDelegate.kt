package eu.peernetwork.user.remote.api

import eu.peernetwork.core.remote.exception.NetworkException
import eu.peernetwork.user.data.api.ResourceApi
import eu.peernetwork.user.domain.exception.ResourceNotFoundException
import okhttp3.OkHttpClient
import okhttp3.Request
import javax.inject.Inject
import javax.inject.Named

class ResourceApiDelegate @Inject constructor(
    @Named("mediaUrl") private val url: String,
    private val client: OkHttpClient
) : ResourceApi {
    override suspend fun string(path: String): String {
        val request = Request.Builder().url("$url$path").build()
        val response = client.newCall(request).execute()
        if (!response.isSuccessful) {
            throw NetworkException(response.message)
        }
        return response.body?.string() ?: throw ResourceNotFoundException()
    }
}
