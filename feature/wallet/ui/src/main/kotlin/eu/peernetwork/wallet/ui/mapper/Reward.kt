@file:JvmName("UiRewardKt")

package eu.peernetwork.wallet.ui.mapper

import eu.peernetwork.wallet.domain.model.Reward
import eu.peernetwork.wallet.ui.model.UiReward

fun Reward.mapFromDomain(): UiReward {
    return UiReward(
        name = type,
        used = used,
        available = available
    )
}
