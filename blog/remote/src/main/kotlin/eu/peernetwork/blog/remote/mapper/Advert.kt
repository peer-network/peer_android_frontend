package eu.peernetwork.blog.remote.mapper

import eu.peernetwork.blog.domain.model.AdPlan
import type.AdvertisementPinnedPlan
import type.AdvertisementType

fun AdPlan.mapFromDomain(): AdvertisementPinnedPlan {
    return when (this) {
        AdPlan.PIN -> AdvertisementPinnedPlan.PINNED
    }
}

fun AdvertisementType.mapToDomain(): AdPlan {
    return when (this) {
        AdvertisementType.PINNED -> AdPlan.PIN
        else -> AdPlan.PIN
    }
}
