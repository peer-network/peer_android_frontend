package eu.peernetwork.wallet.remote.mapper

import eu.peernetwork.wallet.domain.model.Sort
import wallet.type.SortFilterType

fun Sort.mapFromDomain(): SortFilterType? {
    return when(this) {
        Sort.NEWEST -> SortFilterType.NEWEST
        Sort.OLDEST -> SortFilterType.OLDEST
    }
}
