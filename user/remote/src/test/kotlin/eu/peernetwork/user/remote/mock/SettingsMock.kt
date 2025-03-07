package eu.peernetwork.user.remote.mock

import eu.peernetwork.core.remote.model.Status
import protected.eu.peernetwork.user.remote.UpdateBiographyMutation
import protected.eu.peernetwork.user.remote.UpdateMailMutation
import protected.eu.peernetwork.user.remote.UpdateNameMutation
import protected.eu.peernetwork.user.remote.UpdateProfilePictureMutation

object SettingsMock {
    fun username(): UpdateNameMutation.UpdateName {
        return UpdateNameMutation.UpdateName(
            status = Status.SUCCESS.value,
            ResponseCode = null,
        )
    }

    fun email(): UpdateMailMutation.UpdateMail {
        return UpdateMailMutation.UpdateMail(
            status = Status.SUCCESS.value,
            ResponseCode = null,
        )
    }

    fun bio(): UpdateBiographyMutation.UpdateBiography {
        return UpdateBiographyMutation.UpdateBiography(
            status = Status.SUCCESS.value,
            ResponseCode = null,
        )
    }

    fun avatar(): UpdateProfilePictureMutation.UpdateProfilePicture {
        return UpdateProfilePictureMutation.UpdateProfilePicture(
            status = Status.SUCCESS.value,
            ResponseCode = null,
        )
    }
}
