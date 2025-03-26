package eu.peernetwork.user.remote.api

import com.apollographql.apollo3.ApolloClient
import eu.peernetwork.core.remote.exception.NetworkException
import eu.peernetwork.core.remote.extension.assertOrThrow
import eu.peernetwork.core.remote.extension.executeOrThrow
import eu.peernetwork.core.remote.extension.getOrThrow
import eu.peernetwork.user.data.api.ResourceApi
import eu.peernetwork.user.domain.exception.ResourceNotFoundException
import eu.peernetwork.user.domain.model.Point
import okhttp3.OkHttpClient
import okhttp3.Request
import protected.eu.peernetwork.user.remote.DailyfreestatusQuery
import javax.inject.Inject
import javax.inject.Named

class ResourceApiDelegate @Inject constructor(
    @Named("mediaUrl") private val url: String,
    private val client: OkHttpClient,
    private val apolloClient: ApolloClient
) : ResourceApi {
    override suspend fun points(): List<Point> {
        val response = apolloClient.query(DailyfreestatusQuery()).executeOrThrow()
        val data = response.getOrThrow().dailyfreestatus
        response.assertOrThrow(data.status, data.ResponseCode)
        return data.affectedRows?.mapNotNull {
            Point(
                type = it!!.name,
                used = it.used,
                available = it.available
            )
        } ?: emptyList()
    }

    override suspend fun string(path: String): String {
        val request = Request.Builder().url("$url$path").build()
        val response = client.newCall(request).execute()
        if (!response.isSuccessful) {
            throw NetworkException(response.message)
        }
        return response.body?.string() ?: throw ResourceNotFoundException()
    }
}
