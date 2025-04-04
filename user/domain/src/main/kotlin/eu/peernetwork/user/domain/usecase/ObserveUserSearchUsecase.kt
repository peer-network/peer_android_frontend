package eu.peernetwork.user.domain.usecase

import eu.peernetwork.core.common.usecase.ObservableUseCase
import eu.peernetwork.user.domain.model.User
import eu.peernetwork.user.domain.repository.SearchRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveUserSearchUsecase @Inject constructor(
    private val repository: SearchRepository
) : ObservableUseCase<List<User>> {
    override fun invoke(): Flow<List<User>> {
        return repository.observe()
    }
}