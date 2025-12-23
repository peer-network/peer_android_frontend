package eu.peernetwork.ads.ui.mapper

import eu.peernetwork.ads.domain.model.Campaign
import eu.peernetwork.ads.ui.model.UiCampaign

fun Campaign.mapFromDomain(): UiCampaign {
    return UiCampaign(
        ads = ads.mapToDomain(),
        metrics = metrics.mapToDomain()
    )
}
