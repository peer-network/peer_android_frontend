package eu.peernetwork.user.remote.mock

import eu.peernetwork.core.remote.model.Status
import eu.peernetwork.user.domain.model.AccountDetail
import protected.eu.peernetwork.user.remote.DeleteAccountMutation
import protected.eu.peernetwork.user.remote.ProfileQuery
import protected.eu.peernetwork.user.remote.UpdatePasswordMutation
import public.eu.peernetwork.user.remote.RegisterMutation
import public.eu.peernetwork.user.remote.VerifiedAccountMutation

object AccountMock {
    fun user(): AccountDetail {
        return AccountDetail(
            email = "<test-status>",
            username = "<test-status>",
            password = "<test-status>"
        )
    }

    fun profile(): ProfileQuery.Profile {
        return ProfileQuery.Profile(
            status = Status.SUCCESS.value,
            ResponseCode = null,
            affectedRows = ProfileQuery.AffectedRows(
                id = "<test-id>",
                username = "<test-username>",
                slug = System.currentTimeMillis().toInt(),
                img = "<test-img>",
                biography = "<test-biography>"
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

    fun verification(): VerifiedAccountMutation.VerifiedAccount {
        return VerifiedAccountMutation.VerifiedAccount(
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
