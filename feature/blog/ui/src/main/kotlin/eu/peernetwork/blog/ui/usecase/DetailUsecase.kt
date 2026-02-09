package eu.peernetwork.blog.ui.usecase

import eu.peernetwork.blog.domain.usecase.ContentUsecase
import eu.peernetwork.blog.ui.mapper.mapFromDomain
import eu.peernetwork.blog.ui.model.UiPost
import eu.peernetwork.core.common.usecase.ParameterizedSuspendableUseCase
import javax.inject.Inject

class DetailUsecase  @Inject constructor(
    private val usecase: ContentUsecase,
) : ParameterizedSuspendableUseCase<String, UiPost> {
    override suspend fun invoke(param: String): UiPost {
        return usecase(param).mapFromDomain()
    }
}
