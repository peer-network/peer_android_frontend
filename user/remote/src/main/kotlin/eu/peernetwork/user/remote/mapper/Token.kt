package eu.peernetwork.user.remote.mapper

import eu.peernetwork.user.domain.model.Token
import public.eu.peernetwork.user.remote.LoginMutation
import public.eu.peernetwork.user.remote.RefreshTokenMutation
import java.util.concurrent.TimeUnit

fun LoginMutation.Login.mapToDomain(): Token {
    return Token(
        access = accessToken ?: "",
        refresh = refreshToken ?: "",
        expiresIn = TimeUnit.DAYS.toSeconds(7)
    )
}

fun RefreshTokenMutation.RefreshToken.mapToDomain(): Token {
    return Token(
        access = accessToken ?: "",
        refresh = refreshToken ?: "",
        expiresIn = TimeUnit.DAYS.toSeconds(7)
    )
}
