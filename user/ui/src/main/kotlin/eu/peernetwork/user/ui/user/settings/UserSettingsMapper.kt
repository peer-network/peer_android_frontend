package eu.peernetwork.user.ui.user.settings

import eu.peernetwork.user.ui.model.UiAccount

fun UiAccount.toSettings(): List<UserSettingsModel> {
    return listOf(
        UserSettingsModel.Avatar(null),
        UserSettingsModel.Username(username),
        UserSettingsModel.Description(bio ?: "")
    )
}
