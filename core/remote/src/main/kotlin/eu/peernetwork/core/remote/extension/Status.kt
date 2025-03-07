package eu.peernetwork.core.remote.extension

import eu.peernetwork.core.remote.model.Status

fun String.mapToDomain(): Status {
    return when(this) {
        Status.SUCCESS.value.lowercase() -> Status.SUCCESS
        Status.ERROR.value.lowercase() -> Status.ERROR
        else -> Status.UNKNOWN
    }
}
