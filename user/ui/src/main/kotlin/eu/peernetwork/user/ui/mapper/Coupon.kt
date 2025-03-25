package eu.peernetwork.user.ui.mapper

import eu.peernetwork.user.domain.model.Coupon
import eu.peernetwork.user.ui.model.UiCoupon

fun Coupon.mapFromDomain(): UiCoupon {
    return UiCoupon(
        name = name,
        used = used,
        available = available
    )
}
