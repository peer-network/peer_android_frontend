package eu.peernetwork.user.remote.mapper

import protected.type.ContentFilterType


fun String?.mapToScope(): ContentFilterType {
    return this?.let {
        ContentFilterType.safeValueOf(it)
    } ?: ContentFilterType.MYGRANDMALIKES
}
