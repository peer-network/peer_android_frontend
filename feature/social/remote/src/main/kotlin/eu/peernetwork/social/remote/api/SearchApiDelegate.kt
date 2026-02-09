package eu.peernetwork.social.remote.api

import com.apollographql.apollo3.api.Optional
import eu.peernetwork.core.common.paging.Page
import eu.peernetwork.core.common.paging.Pageable
import eu.peernetwork.core.remote.extension.assertOrThrow
import eu.peernetwork.core.remote.extension.executeOrThrow
import eu.peernetwork.core.remote.extension.getOrThrow
import eu.peernetwork.core.remote.api.RequestClient
import eu.peernetwork.social.data.api.SearchApi
import eu.peernetwork.social.domain.model.Post
import eu.peernetwork.social.domain.model.Tag
import eu.peernetwork.social.remote.mapper.mapToDomain
import social.social.eu.peernetwork.social.remote.GetallpostsQuery
import social.social.eu.peernetwork.social.remote.SearchTagsQuery
import javax.inject.Inject
import javax.inject.Named

class SearchApiDelegate @Inject constructor(
    @Named("mediaUrl") private val url: String,
    private val client: RequestClient,
) : SearchApi {
    override suspend fun findAllTags(tag: String, pageable: Pageable): Page<Tag> {
        val query = SearchTagsQuery(
            tagName = tag,
            offset = pageable.offset,
            limit = pageable.limit
        )
        val response = client().query(query).executeOrThrow()
        val data = response.getOrThrow().searchTags
        val contents = data.affectedRows?.mapNotNull {
            it?.name?.let { Tag(it) }
        } ?: emptyList()
        response.assertOrThrow(data.status, data.ResponseCode)
        return Page(
            count = data.counter,
            offset = pageable.offset,
            items = contents
        )
    }

    override suspend fun findPostsByTitle(title: String, pageable: Pageable): Page<Post> {
        val query = GetallpostsQuery(
            title = Optional.present(title),
            offset = Optional.present(pageable.offset),
            limit = Optional.present(pageable.limit)
        )
        val response = client().query(query).executeOrThrow()
        val data = response.getOrThrow().listPosts
        val contents = data.affectedRows?.map { it.mapToDomain(url) }
        response.assertOrThrow(data.status, data.ResponseCode)
        return Page(
            count = data.counter,
            offset = pageable.offset,
            items = contents ?: emptyList()
        )
    }
}
