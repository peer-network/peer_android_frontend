package eu.peernetwork.user.ui.user.settings

import eu.peernetwork.user.ui.model.UiAccount

fun UiAccount.mapToModels(): List<UserSettingsModel> {
    return listOf(
        UserSettingsModel.Avatar(null),
        UserSettingsModel.Username(username),
        UserSettingsModel.Description(bio ?: "")
    )
}

fun UiAccount.isPasswordRequired(model: List<UserSettingsModel>): Boolean {
    val mapper = mapToModels().associateBy { it.name }
    model.forEach {
        if (mapper[it.name]?.value != it.value) {
            if (it.protected) {
                return true
            }
        }
    }
    return false
}
