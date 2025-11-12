package eu.peernetwork.ads.remote.mapper

import ads.type.AdvertisementSort
import eu.peernetwork.ads.domain.model.Filter
import eu.peernetwork.ads.domain.model.Sort

fun Filter.sortType(): AdvertisementSort? {
    return sort.mapFromDomain()
}

fun Sort.mapFromDomain(): AdvertisementSort? {
    return when (this) {
        Sort.NEWEST -> AdvertisementSort.NEWEST
        Sort.BIGGEST_COST -> AdvertisementSort.BIGGEST_COST
        Sort.SMALLEST_COST -> AdvertisementSort.SMALLEST_COST
        Sort.OLDEST -> AdvertisementSort.OLDEST
    }
}

