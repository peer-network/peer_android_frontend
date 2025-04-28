package eu.peernetwork.social.domain.usecase

import eu.peernetwork.core.common.model.Pageable
import eu.peernetwork.core.common.usecase.ParameterizedSuspendableUseCase
import eu.peernetwork.social.domain.repository.SearchRepository
import javax.inject.Inject

class UserSearchUsecase @Inject constructor(
    private val repository: SearchRepository
) : ParameterizedSuspendableUseCase<String, Unit> {
    override suspend fun invoke(param: String) {
        repository.filterByUsername(param, Pageable(0, 20))
    }
}