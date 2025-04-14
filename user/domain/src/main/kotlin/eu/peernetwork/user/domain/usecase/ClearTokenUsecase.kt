package eu.peernetwork.user.domain.usecase

import eu.peernetwork.core.common.usecase.SuspendableUseCase
import eu.peernetwork.user.domain.repository.TokenRepository
import javax.inject.Inject

class ClearTokenUsecase @Inject constructor(
    private val repository: TokenRepository
) : SuspendableUseCase<Unit> {
    override suspend fun invoke() {
        return repository.clear()
    }
}
