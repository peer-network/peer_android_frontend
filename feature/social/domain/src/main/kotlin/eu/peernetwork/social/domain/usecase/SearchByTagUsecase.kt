package eu.peernetwork.social.domain.usecase

import eu.peernetwork.core.common.paging.Page
import eu.peernetwork.core.common.paging.Pageable
import eu.peernetwork.core.common.usecase.ParameterizedSuspendableUseCase
import eu.peernetwork.social.domain.model.Tag
import eu.peernetwork.social.domain.repository.SearchRepository
import javax.inject.Inject

class SearchByTagUsecase @Inject constructor(
    private val repository: SearchRepository
) : ParameterizedSuspendableUseCase<SearchByTagUsecase.Parameter, Page<Tag>> {
    override suspend fun invoke(param: Parameter): Page<Tag> {
        return repository.findAllTags(param.tag, param.page)
    }

    data class Parameter(
        val tag: String,
        val page: Pageable
    )
}
