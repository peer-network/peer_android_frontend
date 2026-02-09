package eu.peernetwork.ads.ui.usecase

import eu.peernetwork.ads.domain.usecase.ContentUsecase
import eu.peernetwork.ads.ui.mapper.mapToDomain
import eu.peernetwork.ads.ui.model.UiContent
import eu.peernetwork.core.common.usecase.ParameterizedSuspendableUseCase
import javax.inject.Inject

class ArticleUsecase @Inject constructor(
    private val usecase: ContentUsecase
) : ParameterizedSuspendableUseCase<String, UiContent> {
    override suspend fun invoke(param: String): UiContent {
        return usecase(param).mapToDomain()
    }
}
