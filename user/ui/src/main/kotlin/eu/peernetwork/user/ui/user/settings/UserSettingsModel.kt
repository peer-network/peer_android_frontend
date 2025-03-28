package eu.peernetwork.user.ui.user.settings

import android.net.Uri
import androidx.compose.runtime.Immutable

@Immutable
sealed class UserSettingsModel(
    val name: String,
    val value: Any?,
    val protected: Boolean
) {
    data class Avatar(val image: Uri?): UserSettingsModel(
        name = AVATAR,
        value = image,
        protected = false
    )
    data class Username(val username: String): UserSettingsModel(
        name = USERNAME,
        value = username,
        protected = true
    )
    data class Description(val description: String): UserSettingsModel(
        name = BIO,
        value = description,
        protected = false
    )

    companion object {
        const val AVATAR = "avatar"
        const val USERNAME = "username"
        const val BIO = "description"
    }
}
