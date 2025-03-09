package eu.peernetwork.user.domain.usecase

import eu.peernetwork.core.common.usecase.ImmediateUseCase
import eu.peernetwork.user.domain.model.Token
import eu.peernetwork.user.domain.repository.TokenRepository
import javax.inject.Inject

class TokenUsecase @Inject constructor(
    private val repository: TokenRepository
) : ImmediateUseCase<Token?> {
    override fun invoke(): Token? {
        return repository.get()
    }
}
