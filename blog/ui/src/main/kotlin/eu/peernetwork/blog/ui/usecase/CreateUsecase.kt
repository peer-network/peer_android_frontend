package eu.peernetwork.blog.ui.usecase

import android.content.Context
import eu.peernetwork.blog.domain.interactor.ContentInteractor
import eu.peernetwork.blog.domain.model.Draft
import eu.peernetwork.blog.ui.mapper.mapToPhoto
import eu.peernetwork.blog.ui.model.UiPost
import eu.peernetwork.core.common.provider.Dispatcher
import eu.peernetwork.core.common.usecase.ParameterizedSuspendableUseCase
import eu.peernetwork.core.ui.usecase.AnnotationUsecase
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CreateUsecase @Inject constructor(
    private val context: Context,
    private val dispatcher: Dispatcher,
    private val interactor: ContentInteractor,
    private val annotationUsecase: AnnotationUsecase
) : ParameterizedSuspendableUseCase<Draft, UiPost> {
    override suspend fun invoke(param: Draft): UiPost = withContext(dispatcher.io) {
        interactor.create(param).mapToPhoto(context) { annotationUsecase(it) }
    }
}
