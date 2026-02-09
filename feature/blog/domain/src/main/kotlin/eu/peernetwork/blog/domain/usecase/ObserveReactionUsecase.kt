package eu.peernetwork.blog.domain.usecase

import eu.peernetwork.blog.domain.interactor.EngagementInteractor
import eu.peernetwork.blog.domain.interactor.EngagementInteractor.Reaction
import eu.peernetwork.core.common.usecase.ObservableUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveReactionUsecase @Inject constructor(
    private val interactor: EngagementInteractor
) : ObservableUseCase<Map<String, Reaction>> {
    override fun invoke(): Flow<Map<String, Reaction>> {
        return interactor.observe()
    }
}
