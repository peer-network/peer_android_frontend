package eu.peernetwork.user.remote.mock

import eu.peernetwork.core.remote.model.Status
import protected.eu.peernetwork.user.remote.PreferenceQuery
import protected.type.ContentFilterType

object PreferenceMock {
    fun get(): PreferenceQuery.GetUserInfo {
        return PreferenceQuery.GetUserInfo(
            status = Status.SUCCESS.value,
            ResponseCode = "1101",
            affectedRows = PreferenceQuery.AffectedRows(
                userPreferences = PreferenceQuery.UserPreferences(
                    ContentFilterType.MYGRANDMALIKES
                )
            )
        )
    }
}
