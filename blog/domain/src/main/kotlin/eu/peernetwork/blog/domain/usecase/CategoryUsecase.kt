package eu.peernetwork.blog.domain.usecase

import eu.peernetwork.blog.domain.model.Content
import eu.peernetwork.blog.domain.model.Category
import eu.peernetwork.core.common.usecase.ParameterizedSuspendableUseCase
import javax.inject.Inject
import kotlin.collections.plus

class CategoryUsecase @Inject constructor() : ParameterizedSuspendableUseCase<CategoryUsecase.Parameter, Set<Content.Type>> {
    override suspend fun invoke(param: Parameter): Set<Content.Type> {
        return when (param.category) {
            Category.FOLLOWED  -> param.types + Content.Type.FOLLOWED
            Category.FOLLOWER  -> param.types + Content.Type.FOLLOWER
            Category.ALL,
            Category.MOST_LIKED,
            Category.MOST_DISLIKED -> param.types
        }
    }

    data class Parameter(
        val types: Set<Content.Type>,
        val category: Category,
    )
}
