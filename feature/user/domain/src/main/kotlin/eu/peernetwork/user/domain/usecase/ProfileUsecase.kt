package eu.peernetwork.user.domain.usecase

import eu.peernetwork.core.common.usecase.ParameterizedSuspendableUseCase
import eu.peernetwork.user.domain.model.Account
import eu.peernetwork.user.domain.repository.AccountRepository
import javax.inject.Inject

class ProfileUsecase @Inject constructor(
    private val repository: AccountRepository
) : ParameterizedSuspendableUseCase<String, Account> {
    override suspend fun invoke(param: String): Account {
        return repository.get(param)
    }
}
