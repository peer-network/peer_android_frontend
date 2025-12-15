package eu.peernetwork.ads.remote.api

import ads.ads.eu.peernetwork.ads.remote.AdvertisePostPinnedMutation
import ads.ads.eu.peernetwork.ads.remote.AdvertisementHistoryQuery
import ads.type.AdvertisementHistoryFilter
import ads.type.AdvertisementPinnedPlan
import com.apollographql.apollo3.api.Optional
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import eu.peernetwork.ads.data.api.AdvertiserApi
import eu.peernetwork.ads.domain.model.Ads
import eu.peernetwork.ads.domain.model.Campaign
import eu.peernetwork.ads.domain.model.AdsList
import eu.peernetwork.ads.domain.model.Description
import eu.peernetwork.ads.domain.model.Filter
import eu.peernetwork.ads.domain.model.Metrics
import eu.peernetwork.ads.domain.model.Order
import eu.peernetwork.ads.remote.mapper.mapToAds
import eu.peernetwork.ads.remote.mapper.mapToContent
import eu.peernetwork.ads.remote.mapper.mapToDomain
import eu.peernetwork.ads.remote.mapper.mapToMetrics
import eu.peernetwork.ads.remote.mapper.sortType
import eu.peernetwork.ads.remote.model.DescriptionModel
import eu.peernetwork.ads.remote.model.MediaModel
import eu.peernetwork.core.common.exception.ContentException
import eu.peernetwork.core.common.paging.Pageable
import eu.peernetwork.core.remote.api.RequestClient
import eu.peernetwork.core.remote.exception.NetworkException
import eu.peernetwork.core.remote.extension.assertOrThrow
import eu.peernetwork.core.remote.extension.executeOrThrow
import eu.peernetwork.core.remote.extension.getOrThrow
import okhttp3.OkHttpClient
import okhttp3.Request
import javax.inject.Inject
import javax.inject.Named

class AdvertiserApiDelegate @Inject constructor(
    private val gson: Gson,
    @Named("mediaUrl") private val url: String,
    private val client: RequestClient,
    private val http: OkHttpClient
) : AdvertiserApi {
    override suspend fun get(id: String): Campaign {
        val filter = Filter(adsId = id)
        val page = Pageable(
            offset = 0,
            limit = 1
        )
        val response = getAll(filter, page)
        Campaign(
            ads = response.items.first(),
            metrics = response.metrics
        )
        return Campaign(
            ads = response.items.first(),
            metrics = response.metrics
        )
    }

    override suspend fun getAll(filter: Filter, page: Pageable): AdsList {
        val query = getQuery(filter, page)
        val response = client().query(query).executeOrThrow()
        val data = response.getOrThrow().advertisementHistory
        val rows = data.affectedRows
        val metrics = rows?.stats?.mapToDomain() ?: throw ContentException()
        val contents = rows.advertisements?.filterNotNull()?.map {
            val ads = it.mapToAds()
            val content = it.mapToContent()
            val adsMetrics = it.mapToMetrics()
            Ads(
                id = ads.id,
                from = ads.from,
                to = ads.to,
                status = ads.status,
                cost = ads.cost,
                earning = ads.earning,
                metrics = adsMetrics,
                content = content.mapToDomain().copy(
                    path = gson.fromJson<List<MediaModel>>(
                        content.path,
                        object : TypeToken<List<MediaModel>>() {}.type
                    ).map { media ->
                        media.copy(path = "$url${media.path}")
                    }.first().path
                )
            )
        }
        response.assertOrThrow(data.status, data.ResponseCode)
        return AdsList(
            count = page.limit,
            offset = page.offset,
            metrics = metrics,
            items = contents ?: emptyList()
        )
    }

    fun getQuery(filter: Filter, page: Pageable): AdvertisementHistoryQuery {
        val post = filter.adsId?.let { Optional.present(it) } ?: Optional.absent()
        val author = filter.author?.let { Optional.present(it) } ?: Optional.absent()
        val sortBy = filter.sortType()?.let {
            Optional.present(it)
        } ?: Optional.absent()
        val filterBy = filter.criteria?.let { criteria ->
            (criteria as? Filter.Criteria.Content?)?.let {
                AdvertisementHistoryFilter(
                    from = Optional.presentIfNotNull(it.from),
                    to = Optional.presentIfNotNull(it.to),
                    advertisementId = post,
                    userId = author
                )
            }
        } ?: AdvertisementHistoryFilter(
            advertisementId = post,
            userId = author
        )
        return AdvertisementHistoryQuery(
            filter = Optional.presentIfNotNull(filterBy),
            sort = sortBy,
            offset = Optional.present(page.offset),
            limit = Optional.present(page.limit)
        )
    }

    override suspend fun getMetrics(filter: Filter): Metrics {
        val page = Pageable(
            offset = 0,
            limit = 1
        )
        val query = getQuery(filter, page)
        val response = client().query(query).executeOrThrow()
        val data = response.getOrThrow().advertisementHistory
        return data.affectedRows?.stats?.mapToDomain() ?: throw ContentException()
    }

    override suspend fun create(id: String): Order {
        val mutation = AdvertisePostPinnedMutation(
            postId = id,
            advertisePlan = AdvertisementPinnedPlan.PINNED
        )
        val response = client().mutation(mutation).executeOrThrow()
        val data = response.getOrThrow().advertisePostPinned
        return data.affectedRows?.map { it?.mapToDomain() }?.firstOrNull() ?: throw ContentException()
    }

    override suspend fun description(): Description {
        val request = Request.Builder().url("$url/assets/constants.json").build()
        val response = http.newCall(request).execute()
        if (!response.isSuccessful) {
            throw NetworkException(response.message)
        }
        return response.body?.string()?.let {
            gson.fromJson(it, DescriptionModel::class.java).mapToDomain()
        } ?: throw ContentException()
    }
}
