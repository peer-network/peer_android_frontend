package eu.peernetwork.blog.domain.usecase

import eu.peernetwork.blog.domain.model.Comment
import eu.peernetwork.blog.domain.repository.CommentRepository
import eu.peernetwork.core.common.usecase.ParameterizedSuspendableUseCase
import javax.inject.Inject

class CommentUsecase @Inject constructor(
    private val repository: CommentRepository
) : ParameterizedSuspendableUseCase<CommentUsecase.Parameter, Comment> {
    override suspend fun invoke(param: Parameter): Comment {
        return repository.comment(param.id, param.text)
    }

    data class Parameter(val id: String, val text: String)
}