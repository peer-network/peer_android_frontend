package eu.peernetwork.ads.remote.api

import ads.ads.eu.peernetwork.ads.remote.AdvertisePostPinnedMutation
import ads.ads.eu.peernetwork.ads.remote.AdvertisementHistoryQuery
import ads.type.AdvertisementHistoryFilter
import ads.type.AdvertisementPinnedPlan
import com.apollographql.apollo3.api.Optional
import com.google.gson.Gson
import eu.peernetwork.ads.data.api.AdvertiserApi
import eu.peernetwork.ads.domain.model.Ads
import eu.peernetwork.ads.domain.model.Filter
import eu.peernetwork.ads.domain.model.Metrics
import eu.peernetwork.ads.remote.mapper.mapToAds
import eu.peernetwork.ads.remote.mapper.mapToContent
import eu.peernetwork.ads.remote.mapper.mapToDomain
import eu.peernetwork.ads.remote.mapper.sortType
import eu.peernetwork.core.common.exception.ContentException
import eu.peernetwork.core.common.paging.Page
import eu.peernetwork.core.common.paging.Pageable
import eu.peernetwork.core.remote.api.RequestClient
import eu.peernetwork.core.remote.extension.assertOrThrow
import eu.peernetwork.core.remote.extension.executeOrThrow
import eu.peernetwork.core.remote.extension.getOrThrow
import javax.inject.Inject
import javax.inject.Named

class AdvertiserApiDelegate @Inject constructor(
    private val gson: Gson,
    @Named("mediaUrl") private val url: String,
    private val client: RequestClient
) : AdvertiserApi {
    override suspend fun get(id: String): Ads {
        val filter = Filter(postId = id)
        val page = Pageable(
            offset = 0,
            limit = 1
        )
        return getAll(filter, page).items.first()
    }

    override suspend fun getAll(filter: Filter, page: Pageable): Page<Ads> {
        val query = getQuery(filter, page)
        val response = client().query(query).executeOrThrow()
        val data = response.getOrThrow().advertisementHistory
        val contents = data.affectedRows?.advertisements?.filterNotNull()?.map {
            val adds = it.mapToAds()
            val content = it.mapToContent()
            Ads(
                from = adds.from,
                to = adds.to,
                status = adds.status,
                cost = adds.cost,
                earning = adds.earning,
                content = content.mapToDomain()
            )
        }
        response.assertOrThrow(data.status, data.ResponseCode)
        return Page(
            count = page.limit,
            offset = page.offset,
            items = contents ?: emptyList()
        )
    }

    fun getQuery(filter: Filter, page: Pageable): AdvertisementHistoryQuery {
        val post = filter.postId?.let { Optional.present(it) } ?: Optional.absent()
        val author = filter.author?.let { Optional.present(it) } ?: Optional.absent()
        val sortBy = filter.sortType()?.let {
            Optional.present(it)
        } ?: Optional.absent()
        val filterBy = filter.criteria?.let { criteria ->
            (criteria as? Filter.Criteria.Content?)?.let {
                AdvertisementHistoryFilter(
                    from = Optional.presentIfNotNull(it.from),
                    to = Optional.presentIfNotNull(it.to),
                    advertisementId = Optional.presentIfNotNull(it.to),
                    postId = post,
                    userId = author
                )
            }
        }
        return AdvertisementHistoryQuery(
            filter = Optional.presentIfNotNull(filterBy),
            sort = sortBy,
            offset = Optional.present(page.offset),
            limit = Optional.present(page.limit)
        )
    }

    override suspend fun getMetrics(author: String): Metrics {
        val filter = Filter(author = author)
        val page = Pageable(
            offset = 0,
            limit = 1
        )
        val query = getQuery(filter, page)
        val response = client().query(query).executeOrThrow()
        val data = response.getOrThrow().advertisementHistory
        return data.affectedRows?.stats?.mapToDomain() ?: throw ContentException()
    }

    override suspend fun create(id: String) {
        val mutation = AdvertisePostPinnedMutation(
            postId = id,
            advertisePlan = AdvertisementPinnedPlan.PINNED
        )
        val response = client().mutation(mutation).executeOrThrow()
        response.getOrThrow().advertisePostPinned
    }
}
