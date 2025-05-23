package eu.peernetwork.user.remote.mock

import eu.peernetwork.core.remote.model.Status
import eu.peernetwork.user.domain.model.UserDetail
import protected.eu.peernetwork.user.remote.DeleteAccountMutation
import protected.eu.peernetwork.user.remote.ProfileQuery
import protected.eu.peernetwork.user.remote.UpdatePasswordMutation
import public.eu.peernetwork.user.remote.RegisterMutation
import public.eu.peernetwork.user.remote.RequestPasswordResetMutation
import public.eu.peernetwork.user.remote.ResetPasswordMutation
import public.eu.peernetwork.user.remote.VerifiedAccountMutation

object AccountMock {
    fun user(): UserDetail {
        return UserDetail(
            email = "<test-status>",
            username = "<test-status>",
            password = "<test-status>"
        )
    }

    fun profile(): ProfileQuery.GetProfile {
        return ProfileQuery.GetProfile(
            status = Status.SUCCESS.value,
            ResponseCode = null,
            affectedRows = ProfileQuery.AffectedRows(
                id = "<test-id>",
                username = "<test-username>",
                slug = System.currentTimeMillis().toInt(),
                img = "<test-img>",
                biography = "<test-biography>",
                amountfollowed = 0,
                amountposts = 0,
                amountfollower = 0,
                amountfriends = 0,
                isfollowing = false,
                isfollowed = false
            )
        )
    }

    fun register(): RegisterMutation.Register {
        return RegisterMutation.Register(
            status = Status.SUCCESS.value,
            ResponseCode = null,
            userid = "<test-status>"
        )
    }

    fun password(): UpdatePasswordMutation.UpdatePassword {
        return UpdatePasswordMutation.UpdatePassword(
            status = Status.SUCCESS.value,
            ResponseCode = null,
        )
    }

    fun passwordResetRequest(): RequestPasswordResetMutation.RequestPasswordReset {
        return RequestPasswordResetMutation.RequestPasswordReset(
            status = Status.SUCCESS.value,
            ResponseCode = null,
        )
    }

    fun passwordReset(): ResetPasswordMutation.ResetPassword {
        return ResetPasswordMutation.ResetPassword(
            status = Status.SUCCESS.value,
            ResponseCode = null,
        )
    }

    fun verification(): VerifiedAccountMutation.VerifyAccount {
        return VerifiedAccountMutation.VerifyAccount(
            status = Status.SUCCESS.value,
            ResponseCode = null,
        )
    }

    fun delete(): DeleteAccountMutation.DeleteAccount {
        return DeleteAccountMutation.DeleteAccount(
            status = Status.SUCCESS.value,
            ResponseCode = null,
        )
    }
}
