package eu.peernetwork.user.domain.usecase

import eu.peernetwork.core.common.usecase.ParameterizedSuspendableUseCase
import eu.peernetwork.user.domain.model.Token
import eu.peernetwork.user.domain.repository.TokenRepository
import javax.inject.Inject

class RefreshTokenUsecase @Inject constructor(
    private val repository: TokenRepository
) : ParameterizedSuspendableUseCase<String, Token> {
    override suspend fun invoke(param: String): Token {
        return repository.refresh(param)
    }
}
