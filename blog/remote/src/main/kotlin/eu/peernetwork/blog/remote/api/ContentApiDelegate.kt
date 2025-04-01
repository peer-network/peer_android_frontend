package eu.peernetwork.blog.remote.api

import com.apollographql.apollo3.ApolloClient
import eu.peernetwork.blog.data.api.ContentApi
import eu.peernetwork.blog.domain.model.Content
import eu.peernetwork.blog.domain.model.Draft
import eu.peernetwork.blog.domain.model.Filter
import eu.peernetwork.core.common.model.Pageable
import javax.inject.Inject

class ContentApiDelegate @Inject constructor(
    private val client: ApolloClient
) : ContentApi {
    override suspend fun get(
        filter: Filter,
        page: Pageable
    ): List<Content> {
        TODO("Not yet implemented")
    }

    override suspend fun create(
        draft: Draft,
        media: String,
        cover: String
    ): Content {
        TODO("Not yet implemented")
    }
}
