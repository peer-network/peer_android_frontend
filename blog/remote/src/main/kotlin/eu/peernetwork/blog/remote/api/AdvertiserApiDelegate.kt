package eu.peernetwork.blog.remote.api

import com.apollographql.apollo3.api.Optional
import eu.peernetwork.blog.data.api.AdvertiserApi
import eu.peernetwork.blog.domain.exception.ContentException
import eu.peernetwork.blog.domain.model.AdPlan
import eu.peernetwork.blog.domain.model.Advert
import eu.peernetwork.blog.remote.advert.AdvertisePostPinnedMutation
import eu.peernetwork.blog.remote.mapper.mapFromDomain
import eu.peernetwork.blog.remote.mapper.mapToDomain
import eu.peernetwork.core.remote.api.RequestClient
import eu.peernetwork.core.remote.extension.assertOrThrow
import eu.peernetwork.core.remote.extension.executeOrThrow
import eu.peernetwork.core.remote.extension.getOrThrow
import javax.inject.Inject

class AdvertiserApiDelegate @Inject constructor(
    private val client: RequestClient,
) : AdvertiserApi {
    override suspend fun create(
        id: String,
        ad: AdPlan,
        refresh: Boolean
    ): Advert {
        val mutation = AdvertisePostPinnedMutation(
            postId = id,
            advertisePlan = ad.mapFromDomain(),
            forceUpdate = Optional.present(refresh)
        )
        val response = client().mutation(mutation).executeOrThrow()
        val data = response.getOrThrow().advertisePostPinned
        response.assertOrThrow(data.status, data.ResponseCode)
        return data.affectedRows?.filterNotNull()?.map {
            Advert(
                id = it.id,
                plan = it.type.mapToDomain()
            )
        }?.firstOrNull() ?: throw ContentException()
    }
}
