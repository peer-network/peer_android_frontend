package eu.peernetwork.user.domain.usecase

import eu.peernetwork.core.common.usecase.SuspendableUseCase
import eu.peernetwork.user.domain.interactor.AuthenticationInteractor
import javax.inject.Inject

class PrincipalUsecase @Inject constructor(
    private val repository: AuthenticationInteractor
) : SuspendableUseCase<String> {
    override suspend fun invoke(): String {
        return repository.get()
    }
}
