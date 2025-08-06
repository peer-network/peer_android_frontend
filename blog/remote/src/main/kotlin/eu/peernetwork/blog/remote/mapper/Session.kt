package eu.peernetwork.blog.remote.mapper

import type.ContentFilterType

fun String?.mapToScope(): ContentFilterType {
    return this?.let {
        ContentFilterType.safeValueOf(it)
    } ?: ContentFilterType.MYGRANDMALIKES
}
