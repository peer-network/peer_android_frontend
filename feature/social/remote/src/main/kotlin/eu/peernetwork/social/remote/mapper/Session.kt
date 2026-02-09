package eu.peernetwork.social.remote.mapper

import social.type.ContentFilterType

fun String?.mapToMode(): ContentFilterType {
    return this?.let {
        ContentFilterType.safeValueOf(it)
    } ?: ContentFilterType.MYGRANDMALIKES
}
