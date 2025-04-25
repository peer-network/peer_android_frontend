package eu.peernetwork.user.remote.mapper

import eu.peernetwork.user.domain.model.Token
import public.eu.peernetwork.user.remote.LoginMutation
import public.eu.peernetwork.user.remote.RefreshTokenMutation
import java.util.Date

fun LoginMutation.Login.mapToDomain(): Token {
    return Token(
        access = accessToken ?: "",
        refresh = refreshToken ?: "",
        expiresIn = Date().time
    )
}

fun RefreshTokenMutation.RefreshToken.mapToDomain(): Token {
    return Token(
        access = accessToken ?: "",
        refresh = refreshToken ?: "",
        expiresIn = Date().time
    )
}

fun Token.isExpired(): Boolean {
    return System.currentTimeMillis() / 1000 >= expiresIn
}
