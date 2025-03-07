package eu.peernetwork.user.remote.mock

import eu.peernetwork.core.remote.model.Status
import public.eu.peernetwork.user.remote.LoginMutation

object AuthenticationMock {
    fun login(): LoginMutation.Login {
        return LoginMutation.Login(
            status = Status.SUCCESS.value,
            ResponseCode = null,
            accessToken = "<test-access-token>",
            refreshToken = "<test-refresh-token>"
        )
    }
}
