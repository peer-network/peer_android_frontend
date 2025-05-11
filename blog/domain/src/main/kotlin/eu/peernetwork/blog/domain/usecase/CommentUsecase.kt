package eu.peernetwork.blog.domain.usecase

import eu.peernetwork.blog.domain.interactor.EngagementInteractor
import eu.peernetwork.blog.domain.interactor.PointInteractor
import eu.peernetwork.blog.domain.model.Comment
import eu.peernetwork.blog.domain.repository.CommentRepository
import eu.peernetwork.core.common.usecase.ParameterizedSuspendableUseCase
import javax.inject.Inject

class CommentUsecase @Inject constructor(
    private val repository: CommentRepository,
    private val interactor: PointInteractor,
    private val engagementInteractor: EngagementInteractor
) : ParameterizedSuspendableUseCase<CommentUsecase.Parameter, Comment> {
    override suspend fun invoke(param: Parameter): Comment {
        return try {
            repository.comment(param.id, param.text)
        } finally {
            try {
                engagementInteractor.comment(param.id)
                interactor.refresh()
            } catch (error: Throwable) {
                error.printStackTrace()
            }
        }
    }

    data class Parameter(val id: String, val text: String)
}