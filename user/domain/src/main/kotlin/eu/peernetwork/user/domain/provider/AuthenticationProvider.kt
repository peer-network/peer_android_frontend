package eu.peernetwork.user.domain.provider

import eu.peernetwork.user.domain.interactor.AuthenticationInteractor
import eu.peernetwork.user.domain.repository.AuthenticationRepository
import eu.peernetwork.user.domain.repository.TokenRepository

interface AuthenticationProvider {
    fun authenticationRepository(): AuthenticationRepository

    fun tokenRepository(): TokenRepository

    fun authenticationInteractor(): AuthenticationInteractor
}
