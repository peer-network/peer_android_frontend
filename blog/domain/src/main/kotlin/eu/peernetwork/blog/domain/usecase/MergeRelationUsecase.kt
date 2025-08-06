package eu.peernetwork.blog.domain.usecase

import eu.peernetwork.blog.domain.model.Content
import eu.peernetwork.blog.domain.model.Category
import eu.peernetwork.core.common.usecase.ParameterizedSuspendableUseCase
import javax.inject.Inject
import kotlin.collections.plus

class MergeRelationUsecase @Inject constructor() : ParameterizedSuspendableUseCase<MergeRelationUsecase.Parameter, Set<Content.Type>> {
    override suspend fun invoke(param: Parameter): Set<Content.Type> {
        return when (param.category) {
            Category.FOLLOWED  -> param.types + Content.Type.FOLLOWED
            Category.FOLLOWER  -> param.types + Content.Type.FOLLOWER
            Category.ALL      -> param.types
        }
    }

    data class Parameter(
        val types: Set<Content.Type>,
        val category: Category,
    )
}
