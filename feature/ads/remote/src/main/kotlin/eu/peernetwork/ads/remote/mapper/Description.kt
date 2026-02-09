package eu.peernetwork.ads.remote.mapper

import eu.peernetwork.ads.domain.model.Ads
import eu.peernetwork.ads.domain.model.Description
import eu.peernetwork.ads.remote.model.DescriptionModel

fun DescriptionModel.mapToDomain(): Description {
    return Description(
        plans = listOf(
            Ads.Plan.Basic(data.ads.price.basic),
            Ads.Plan.Pinned(data.ads.price.pinned)
        )
    )
}
