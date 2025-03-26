package eu.peernetwork.user.domain.usecase

import eu.peernetwork.core.common.usecase.SuspendableUseCase
import eu.peernetwork.user.domain.repository.AuthenticationRepository
import javax.inject.Inject

class PrincipalUsecase @Inject constructor(
    private val repository: AuthenticationRepository
) : SuspendableUseCase<String> {
    override suspend fun invoke(): String {
        return repository.authenticated()
    }
}
