package eu.peernetwork.social.domain.usecase

import eu.peernetwork.core.common.model.Pageable
import eu.peernetwork.core.common.usecase.ParameterizedSuspendableUseCase
import eu.peernetwork.social.domain.interactor.SearchInteractor
import javax.inject.Inject

class UserSearchUsecase @Inject constructor(
    private val repository: SearchInteractor
) : ParameterizedSuspendableUseCase<String, Unit> {
    override suspend fun invoke(param: String) {
        repository.user(param, Pageable(0, 20))
    }
}
