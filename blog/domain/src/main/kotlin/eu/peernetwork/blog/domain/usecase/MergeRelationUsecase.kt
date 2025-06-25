package eu.peernetwork.blog.domain.usecase

import eu.peernetwork.blog.domain.model.Content
import eu.peernetwork.blog.domain.model.Relation
import eu.peernetwork.core.common.usecase.ParameterizedSuspendableUseCase
import javax.inject.Inject
import kotlin.collections.plus

class MergeRelationUsecase @Inject constructor() : ParameterizedSuspendableUseCase<MergeRelationUsecase.Parameter, Set<Content.Type>> {
    override suspend fun invoke(param: Parameter): Set<Content.Type> {
        return when (param.relation) {
            Relation.FOLLOWED  -> param.types + Content.Type.FOLLOWED
            Relation.FOLLOWER  -> param.types + Content.Type.FOLLOWER
            Relation.NONE      -> param.types
        }
    }

    data class Parameter(
        val types: Set<Content.Type>,
        val relation: Relation,
    )
}
