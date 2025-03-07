package eu.peernetwork.user.remote.mock

import eu.peernetwork.core.remote.model.Status
import public.eu.peernetwork.user.remote.RefreshTokenMutation

object TokenMock {
    fun refresh(): RefreshTokenMutation.RefreshToken {
        return RefreshTokenMutation.RefreshToken(
            status = Status.SUCCESS.value,
            ResponseCode = null,
            accessToken = "<test-access-token>",
            refreshToken = "<test-refresh-token>"
        )
    }
}
