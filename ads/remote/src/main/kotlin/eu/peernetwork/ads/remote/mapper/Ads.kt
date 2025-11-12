package eu.peernetwork.ads.remote.mapper

import ads.ads.eu.peernetwork.ads.remote.AdvertisementHistoryQuery
import eu.peernetwork.ads.remote.model.AdsModel
import java.math.BigDecimal

fun AdvertisementHistoryQuery.Advertisement.mapToAds(): AdsModel {
    val to = timeframeEnd.mapToTimestamp()
    return AdsModel(
        from = timeframeStart.mapToTimestamp(),
        to = to,
        status = System.currentTimeMillis() < to,
        cost = BigDecimal(totalEuroCost),
        earning = BigDecimal(gemsEarned)
    )
}
