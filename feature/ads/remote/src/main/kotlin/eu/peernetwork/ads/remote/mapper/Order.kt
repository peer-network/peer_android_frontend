package eu.peernetwork.ads.remote.mapper

import ads.ads.eu.peernetwork.ads.remote.AdvertisePostPinnedMutation
import ads.type.AdvertisementType
import eu.peernetwork.ads.domain.model.Ads
import eu.peernetwork.ads.domain.model.Order
import java.math.BigDecimal

fun AdvertisePostPinnedMutation.AffectedRow.mapToDomain(): Order {
    return Order(
        start = timeframeStart.mapToTimestamp(),
        end = timeframeEnd.mapToTimestamp(),
        plan = mapToType(),
        token = BigDecimal(totalTokenCost)
    )
}

fun AdvertisePostPinnedMutation.AffectedRow.mapToType(): Ads.Plan {
    return when(type) {
        AdvertisementType.PINNED -> Ads.Plan.Pinned(price = BigDecimal(totalEuroCost))
        else -> Ads.Plan.Basic(price = BigDecimal(totalEuroCost))
    }
}