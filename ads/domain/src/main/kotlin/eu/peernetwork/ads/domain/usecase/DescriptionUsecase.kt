package eu.peernetwork.ads.domain.usecase

import eu.peernetwork.ads.domain.model.Description
import eu.peernetwork.ads.domain.repository.AdvertiserRepository
import eu.peernetwork.core.common.provider.Dispatcher
import eu.peernetwork.core.common.usecase.SuspendableUseCase
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DescriptionUsecase @Inject constructor(
    private val dispatcher: Dispatcher,
    private val repository: AdvertiserRepository
) : SuspendableUseCase<Description> {
    override suspend fun invoke(): Description = withContext(dispatcher.io) {
        repository.description()
    }
}
