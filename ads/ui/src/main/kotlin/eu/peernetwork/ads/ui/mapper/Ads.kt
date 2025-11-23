package eu.peernetwork.ads.ui.mapper

import eu.peernetwork.ads.domain.model.Ads
import eu.peernetwork.ads.ui.model.UiAds
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Ads.mapToDomain(): UiAds {
    val formatter = SimpleDateFormat("d MMM yyyy", Locale.getDefault())
    return UiAds(
        id = id,
        from = formatter.format(Date(from)),
        to = formatter.format(Date(to)),
        status = status,
        cost = cost,
        earning = earning,
        content = content.mapToDomain()
    )
}
