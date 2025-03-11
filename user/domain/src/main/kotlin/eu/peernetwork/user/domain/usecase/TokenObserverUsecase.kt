package eu.peernetwork.user.domain.usecase

import eu.peernetwork.core.common.usecase.ObservableUseCase
import eu.peernetwork.user.domain.model.Token
import eu.peernetwork.user.domain.repository.TokenRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TokenObserverUsecase @Inject constructor(
    private val repository: TokenRepository
) : ObservableUseCase<Token?> {
    override fun invoke(): Flow<Token?> {
        return repository.observe()
    }
}
