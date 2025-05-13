package eu.peernetwork.user.remote.mock

import eu.peernetwork.core.remote.model.Status
import protected.eu.peernetwork.user.remote.UpdateBiographyMutation
import protected.eu.peernetwork.user.remote.UpdateMailMutation
import protected.eu.peernetwork.user.remote.UpdateNameMutation
import protected.eu.peernetwork.user.remote.UpdateProfilePictureMutation

object SettingsMock {
    fun username(): UpdateNameMutation.UpdateUsername {
        return UpdateNameMutation.UpdateUsername(
            status = Status.SUCCESS.value,
            ResponseCode = null,
        )
    }

    fun email(): UpdateMailMutation.UpdateEmail {
        return UpdateMailMutation.UpdateEmail(
            status = Status.SUCCESS.value,
            ResponseCode = null,
        )
    }

    fun bio(): UpdateBiographyMutation.UpdateBio {
        return UpdateBiographyMutation.UpdateBio(
            status = Status.SUCCESS.value,
            ResponseCode = null,
        )
    }

    fun avatar(): UpdateProfilePictureMutation.UpdateProfileImage {
        return UpdateProfilePictureMutation.UpdateProfileImage(
            status = Status.SUCCESS.value,
            ResponseCode = null,
        )
    }
}
