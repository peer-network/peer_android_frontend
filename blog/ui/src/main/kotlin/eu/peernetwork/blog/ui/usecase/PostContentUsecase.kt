package eu.peernetwork.blog.ui.usecase

import android.content.Context
import eu.peernetwork.blog.domain.usecase.ContentUsecase
import eu.peernetwork.blog.ui.mapper.mapToPhoto
import eu.peernetwork.blog.ui.model.UiPost
import eu.peernetwork.core.common.usecase.ParameterizedSuspendableUseCase
import eu.peernetwork.core.ui.usecase.AnnotationUsecase
import javax.inject.Inject

class PostContentUsecase @Inject constructor(
    private val context: Context,
    private val usecase: ContentUsecase,
    private val annotationUsecase: AnnotationUsecase
) : ParameterizedSuspendableUseCase<String, UiPost> {
    override suspend fun invoke(param: String): UiPost {
        return usecase(param).mapToPhoto(context) { annotationUsecase(it) }
    }
}
