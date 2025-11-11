package eu.peernetwork.blog.remote.api

import com.apollographql.apollo3.api.Optional
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import eu.peernetwork.blog.data.api.ContentApi
import eu.peernetwork.blog.domain.exception.ContentException
import eu.peernetwork.blog.domain.model.Content
import eu.peernetwork.blog.domain.model.Draft
import eu.peernetwork.blog.domain.model.Filter
import eu.peernetwork.blog.remote.advert.ListAdvertisementPostsQuery
import eu.peernetwork.blog.remote.content.CreatePostMutation
import eu.peernetwork.blog.remote.content.GetallpostsQuery
import eu.peernetwork.blog.remote.mapper.mapFromDomain
import eu.peernetwork.blog.remote.mapper.mapToContentType
import eu.peernetwork.blog.remote.mapper.mapToDomain
import eu.peernetwork.blog.remote.mapper.mapToFilter
import eu.peernetwork.blog.remote.mapper.mapToMode
import eu.peernetwork.blog.remote.mapper.sortType
import eu.peernetwork.blog.remote.model.MediaModel
import eu.peernetwork.core.common.interactor.SessionInteractor
import eu.peernetwork.core.common.paging.Page
import eu.peernetwork.core.common.paging.Pageable
import eu.peernetwork.core.remote.extension.assertOrThrow
import eu.peernetwork.core.remote.extension.executeOrThrow
import eu.peernetwork.core.remote.extension.getOrThrow
import eu.peernetwork.core.remote.api.RequestClient
import type.PostType
import javax.inject.Inject
import javax.inject.Named

class ContentApiDelegate @Inject constructor(
    private val gson: Gson,
    @Named("mediaUrl") private val url: String,
    private val client: RequestClient,
    private val interactor: SessionInteractor
) : ContentApi {
    override suspend fun get(filter: Filter, page: Pageable): Page<Content> {
        val post = filter.postId?.let { Optional.present(it) } ?: Optional.absent()
        val author = filter.author?.let { Optional.present(it) } ?: Optional.absent()
        val sortBy = filter.sortType()?.let {
            Optional.present(it)
        } ?: Optional.absent()
        val filterBy = if (filter.type.isEmpty()) {
            Optional.absent()
        } else {
            Optional.present(filter.type.map { it.mapToFilter() } +
                    (filter.category?.mapToFilter()?.let { listOf(it) } ?: listOf()))
        }
        val tag = (filter.criteria as? Filter.Criteria.Content?)?.let { criteria ->
            criteria.tag?.let {
                Optional.present(it)
            } ?: Optional.absent()
        } ?: Optional.absent()
        val title = (filter.criteria as? Filter.Criteria.Content?)?.title?.let {
            Optional.present(it)
        } ?: Optional.absent()
        val query = GetallpostsQuery(
            filter = filterBy,
            sort = sortBy,
            tag = tag,
            title = title,
            postId = post,
            userId = author,
            contentFilterBy = Optional.present(interactor.mode().mapToMode()),
            offset = Optional.present(page.offset),
            limit = Optional.present(page.limit)
        )
        val response = client().query(query).executeOrThrow()
        val data = response.getOrThrow().listPosts
        val contents = data.affectedRows?.map { content ->
            val coverPath = try {
                gson.fromJson<List<Map<String, Any>>>(
                    content.cover,
                    object : TypeToken<List<Map<String, Any>>>() {}.type
                ).firstOrNull()?.get("path") as? String
            } catch (_: Exception) {
                null
            }?.let { "$url$it" }
            content.mapToDomain(
                url,
                gson.fromJson<List<MediaModel>>(
                    content.media,
                    object : TypeToken<List<MediaModel>>() {}.type
                ).map {
                    it.copy(
                        options = it.options?.copy(cover = coverPath)
                    ).mapFromDomain().copy(path = "$url${it.path}")
                }
            )
        }
        response.assertOrThrow(data.status, data.ResponseCode)
        return Page(
            count = data.counter,
            offset = page.offset,
            items = contents ?: emptyList()
        )
    }

    override suspend fun getAdverts(filter: Filter, page: Pageable): Page<Content> {
        val post = filter.postId?.let { Optional.present(it) } ?: Optional.absent()
        val author = filter.author?.let { Optional.present(it) } ?: Optional.absent()
        val tag = (filter.criteria as? Filter.Criteria.Content?)?.let { criteria ->
            criteria.tag?.let {
                Optional.present(it)
            } ?: Optional.absent()
        } ?: Optional.absent()
        val filterBy = if (filter.type.isEmpty()) {
            Optional.absent()
        } else {
            Optional.present(filter.type.map { it.mapToContentType() })
        }
        val query = ListAdvertisementPostsQuery(
            tag = tag,
            postid = post,
            userid = author,
            filterBy = filterBy,
            offset = Optional.present(page.offset),
            limit = Optional.present(page.limit)
        )
        val response = client().query(query).executeOrThrow()
        val data = response.getOrThrow().listAdvertisementPosts
        val contents = data.affectedRows?.map { content ->
            val coverPath = try {
                gson.fromJson<List<Map<String, Any>>>(
                    content.post.cover,
                    object : TypeToken<List<Map<String, Any>>>() {}.type
                ).firstOrNull()?.get("path") as? String
            } catch (_: Exception) {
                null
            }?.let { "$url$it" }
            content.post.mapToDomain(
                url,
                gson.fromJson<List<MediaModel>>(
                    content.post.media,
                    object : TypeToken<List<MediaModel>>() {}.type
                ).map {
                    it.copy(
                        options = it.options?.copy(cover = coverPath)
                    ).mapFromDomain().copy(path = "$url${it.path}")
                }
            )
        }
        response.assertOrThrow(data.status, data.ResponseCode)
        return Page(
            count = data.counter,
            offset = page.offset,
            items = contents ?: emptyList()
        )
    }

    override suspend fun create(draft: Draft, meta: String): Content {
        val mutation = CreatePostMutation(
            action = PostType.POST,
            title = draft.title,
            description = Optional.presentIfNotNull(draft.description),
            contentType = draft.type.mapFromDomain(),
            uploadedFiles = Optional.present(meta),
            cover = draft.getCover(),
            tags = if (draft.tags.isEmpty()) {
                Optional.absent()
            } else { Optional.present(draft.tags) }
        )
        val response = client().mutation(mutation).executeOrThrow()
        val data = response.getOrThrow().createPost
        response.assertOrThrow(data.status, data.ResponseCode)
        val content = data.affectedRows?.mapToDomain(
            url, gson.fromJson<List<MediaModel>>(
                data.affectedRows.media,
                object : TypeToken<List<MediaModel>>() {}.type
            ).map { it.mapFromDomain().copy(path = "$url${it.path}") }
        )
        return content ?: throw ContentException()
    }

    private fun Draft.getCover(): Optional<List<String>> {
        return when (type) {
            is Draft.Type.Text -> Optional.absent()
            is Draft.Type.Video -> Optional.absent()
            is Draft.Type.Audio -> {
                val cover = (type as Draft.Type.Audio).media.mapNotNull { it.cover }
                if (cover.isNotEmpty()) {
                    Optional.present(cover)
                } else {
                    Optional.absent()
                }
            }
            is Draft.Type.Image -> Optional.absent()
        }
    }
}