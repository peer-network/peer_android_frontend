package eu.peernetwork.user.remote.mapper

import eu.peernetwork.user.domain.model.Preference
import protected.eu.peernetwork.user.remote.PreferenceQuery
import protected.type.ContentFilterType

fun PreferenceQuery.UserPreferences.mapToDomain(): Preference {
    return Preference(mode = (contentFilteringSeverityLevel ?: ContentFilterType.MYGRANDMALIKES).name)
}
