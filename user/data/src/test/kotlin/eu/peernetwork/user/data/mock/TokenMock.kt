package eu.peernetwork.user.data.mock

import eu.peernetwork.user.domain.model.Token
import java.util.concurrent.TimeUnit

object TokenMock  {
    fun token(): Token {
        return Token(
            access = "<test-access>",
            refresh = "<test-refresh-token>",
            expiresIn = TimeUnit.DAYS.toSeconds(7)
        )
    }
}
