package eu.peernetwork.blog.domain.usecase

import eu.peernetwork.blog.domain.model.Author
import eu.peernetwork.blog.domain.model.Engagement
import eu.peernetwork.blog.domain.repository.EngagementRepository
import eu.peernetwork.core.common.model.Page
import eu.peernetwork.core.common.model.Pageable
import eu.peernetwork.core.common.usecase.ParameterizedSuspendableUseCase
import javax.inject.Inject

class ContentInteractorUsecase @Inject constructor(
    private val repository: EngagementRepository
) : ParameterizedSuspendableUseCase<ContentInteractorUsecase.Parameter, Page<Author>> {
    override suspend fun invoke(param: Parameter): Page<Author> {
        return repository.reactors(
            id = param.id,
            engagement = param.engagement,
            page = param.page
        )
    }

    data class Parameter(
        val id: String,
        val engagement: Engagement.Content,
        val page: Pageable
    )
}
