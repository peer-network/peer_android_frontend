package eu.peernetwork.ads.remote.api

import ads.ads.eu.peernetwork.ads.remote.ContentQuery
import com.apollographql.apollo3.api.Optional
import com.google.gson.Gson
import eu.peernetwork.ads.data.api.ContentApi
import eu.peernetwork.ads.domain.model.Content
import eu.peernetwork.ads.remote.mapper.mapToDomain
import eu.peernetwork.core.common.exception.ContentException
import eu.peernetwork.core.remote.api.RequestClient
import eu.peernetwork.core.remote.extension.executeOrThrow
import eu.peernetwork.core.remote.extension.getOrThrow
import javax.inject.Inject
import javax.inject.Named

class ContentApiDelegate @Inject constructor(
    private val gson: Gson,
    @Named("mediaUrl") private val url: String,
    private val client: RequestClient
) : ContentApi {
    override suspend fun get(id: String): Content {
        val query = ContentQuery(
            postId = Optional.present(id),
            offset = Optional.present(0),
            limit = Optional.present(1)
        )
        val response = client().query(query).executeOrThrow()
        val data = response.getOrThrow().listPosts
        return data.affectedRows?.firstOrNull()?.mapToDomain() ?: throw ContentException()
    }
}
