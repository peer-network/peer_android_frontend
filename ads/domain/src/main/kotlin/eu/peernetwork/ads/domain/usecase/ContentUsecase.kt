package eu.peernetwork.ads.domain.usecase

import eu.peernetwork.ads.domain.model.Content
import eu.peernetwork.ads.domain.repository.ContentRepository
import eu.peernetwork.core.common.usecase.ParameterizedSuspendableUseCase
import javax.inject.Inject

class ContentUsecase @Inject constructor(
    private val repository: ContentRepository
) : ParameterizedSuspendableUseCase<String, Content> {
    override suspend fun invoke(param: String): Content {
        return repository.get(param)
    }
}
