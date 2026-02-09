package eu.peernetwork.user.domain.usecase

import eu.peernetwork.core.common.usecase.ParameterizedSuspendableUseCase
import eu.peernetwork.user.domain.repository.TokenRepository
import javax.inject.Inject

class VerifyTokenUsecase @Inject constructor(
    private val repository: TokenRepository
) : ParameterizedSuspendableUseCase<String, Unit> {
    override suspend fun invoke(param: String) {
        return repository.verify(param)
    }
}
