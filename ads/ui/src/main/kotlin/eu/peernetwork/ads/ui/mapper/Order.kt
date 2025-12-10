package eu.peernetwork.ads.ui.mapper

import eu.peernetwork.ads.domain.model.Ads
import eu.peernetwork.ads.domain.model.Order
import eu.peernetwork.ads.ui.model.UiOrder
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Order.mapFromDomain(): UiOrder {
    return UiOrder(
        start = start.mapToTimestamp(),
        duration = ((end - start) / (1000 * 60 * 60)).toInt(),
        price = when(plan) {
            is Ads.Plan.Basic -> (plan as Ads.Plan.Basic).price
            is Ads.Plan.Pinned -> (plan as Ads.Plan.Pinned).price
        },
        token = token
    )
}

@Suppress("SimpleDateFormat")
fun Long.mapToTimestamp(pattern: String = "MMM dd, yyyy 'at' HH:mm"): String {
    val formatter = SimpleDateFormat(pattern, Locale.getDefault())
    return formatter.format(Date(this))
}
