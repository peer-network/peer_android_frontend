package eu.peernetwork.blog.remote.api

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.Optional
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import eu.peernetwork.blog.data.api.ContentApi
import eu.peernetwork.blog.domain.exception.ContentException
import eu.peernetwork.blog.domain.model.Content
import eu.peernetwork.blog.domain.model.Draft
import eu.peernetwork.blog.domain.model.Filter
import eu.peernetwork.blog.domain.model.Media
import eu.peernetwork.blog.remote.content.CreatePostMutation
import eu.peernetwork.blog.remote.content.GetallpostsQuery
import eu.peernetwork.blog.remote.mapper.mapFromDomain
import eu.peernetwork.blog.remote.mapper.mapToDomain
import eu.peernetwork.blog.remote.mapper.mapToFilter
import eu.peernetwork.blog.remote.mapper.mapToSortType
import eu.peernetwork.core.common.model.Page
import eu.peernetwork.core.common.model.Pageable
import eu.peernetwork.core.remote.extension.assertOrThrow
import eu.peernetwork.core.remote.extension.executeOrThrow
import eu.peernetwork.core.remote.extension.getOrThrow
import type.PostenType
import javax.inject.Inject
import javax.inject.Named

class ContentApiDelegate @Inject constructor(
    private val gson: Gson,
    @Named("mediaUrl") private val url: String,
    private val client: ApolloClient,
) : ContentApi {
    override suspend fun get(filter: Filter, page: Pageable): Page<Content> {
        val post = filter.postId?.let { Optional.present(it) } ?: Optional.absent()
        val author = filter.author?.let { Optional.present(it) } ?: Optional.absent()
        val sortBy = filter.mapToSortType()?.let {
            Optional.present(it)
        } ?: Optional.absent()
        val filterBy = if (filter.type.isEmpty()) {
            Optional.absent()
        } else {
            Optional.present(filter.type.map { it.mapToFilter() })
        }
        val query = GetallpostsQuery(
            filter = filterBy,
            sort = sortBy,
            postId = post,
            userId = author,
            offset = Optional.present(page.offset),
            limit = Optional.present(page.limit)
        )
        val response = client.query(query).executeOrThrow()
        val data = response.getOrThrow().getallposts
        val contents = data.affectedRows?.map {
            it.mapToDomain(url, gson.fromJson<List<Media>>(
                it.media,
                object : TypeToken<List<Media>>() {}.type
            ).map { it.copy(path = "$url${it.path}") })
        }
        response.assertOrThrow(data.status, data.ResponseCode)
        return Page(
            count = data.counter,
            offset = page.offset,
            items = contents ?: emptyList()
        )
    }

    override suspend fun create(draft: Draft): Content {
        val mutation = CreatePostMutation(
            action = PostenType.POST,
            title = draft.title,
            description = Optional.presentIfNotNull(draft.description),
            contentType = draft.type.mapFromDomain(),
            media = draft.getMedia(),
            cover = draft.getCover(),
            tags = if (draft.tags.isEmpty()) {
                Optional.absent()
            } else { Optional.present(draft.tags) }
        )
        val response = client.mutation(mutation).executeOrThrow()
        val data = response.getOrThrow().createPost
        val content = data.affectedRows?.mapToDomain(
            url, gson.fromJson<List<Media>>(
                data.affectedRows.media,
                object : TypeToken<List<Media>>() {}.type
            ).map { it.copy(path = "$url${it.path}") }
        )
        response.assertOrThrow(data.status, data.ResponseCode)
        return content ?: throw ContentException()
    }

    private fun Draft.getMedia(): Optional<List<String>> {
        return when (type) {
            Draft.Type.Text -> Optional.absent<List<String>>()
            is Draft.Type.Video -> Optional.present((type as Draft.Type.Video).files)
            is Draft.Type.Audio -> Optional.present((type as Draft.Type.Audio).files)
            is Draft.Type.Image -> Optional.present((type as Draft.Type.Image).files)
        }
    }

    private fun Draft.getCover(): Optional<List<String>> {
        return when (type) {
            Draft.Type.Text -> Optional.absent<List<String>>()
            is Draft.Type.Video -> Optional.absent<List<String>>()
            is Draft.Type.Audio -> Optional.present(listOf((type as Draft.Type.Audio).cover))
            is Draft.Type.Image -> Optional.absent<List<String>>()
        }
    }
}
