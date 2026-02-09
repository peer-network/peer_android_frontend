package eu.peernetwork.user.remote.mapper

import eu.peernetwork.user.domain.model.Mode
import eu.peernetwork.user.domain.model.Preference
import protected.eu.peernetwork.user.remote.PreferenceQuery
import protected.type.ContentFilterType
import protected.type.OnboardingType

fun PreferenceQuery.UserPreferences.mapToDomain(): Preference {
    return Preference(
        mode = (contentFilteringSeverityLevel ?: ContentFilterType.MYGRANDMALIKES).mapToDomain(),
        flags = onboardingsWereShown.map { it.rawValue }.toList()
    )
}

fun ContentFilterType.mapToDomain(): Mode {
    return when (this) {
        ContentFilterType.MYGRANDMAHATES -> Mode.Sensitive(ContentFilterType.MYGRANDMAHATES.name)
        else -> Mode.Safe(ContentFilterType.MYGRANDMALIKES.name)
    }
}

fun Mode.mapFromDomain(): ContentFilterType {
    return when (this) {
        is Mode.Sensitive -> ContentFilterType.MYGRANDMAHATES
        else -> ContentFilterType.MYGRANDMALIKES
    }
}

fun String.mapToOnboardingType(): OnboardingType {
    return OnboardingType.safeValueOf(this)
}
