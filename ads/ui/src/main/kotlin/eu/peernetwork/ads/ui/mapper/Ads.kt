package eu.peernetwork.ads.ui.mapper

import eu.peernetwork.ads.domain.model.Ads
import eu.peernetwork.ads.ui.model.UiAds

fun Ads.mapToDomain(): UiAds {
    return UiAds(
        id = id,
        from = from,
        to = to,
        status = status,
        cost = cost,
        earning = earning,
        content = content.mapToDomain()
    )
}
