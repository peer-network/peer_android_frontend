package eu.peernetwork.app.usecase

import eu.peernetwork.app.service.LaunchService
import eu.peernetwork.core.common.provider.Dispatcher
import eu.peernetwork.core.common.usecase.SuspendableUseCase
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ResourceLoaderUsecase @Inject constructor(
    private val loader: LaunchService,
    private val dispatcher: Dispatcher
) : SuspendableUseCase<Unit> {
    override suspend fun invoke() = withContext(dispatcher.io) {
        loader.initialize()
    }
}
