package eu.peernetwork.user.ui.mapper

import eu.peernetwork.user.ui.model.UiAccount
import eu.peernetwork.user.ui.model.UiSettings

fun UiAccount.mapToModels(): List<UiSettings> {
    return listOf(
        UiSettings.Avatar(null),
        UiSettings.Username(username),
        UiSettings.Description(bio ?: "")
    )
}

fun UiAccount.isPasswordRequired(model: List<UiSettings>): Boolean {
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
