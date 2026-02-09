package eu.peernetwork.blog.domain.usecase

import eu.peernetwork.blog.domain.interactor.AuthorInteractor
import eu.peernetwork.blog.domain.model.Author
import eu.peernetwork.core.common.usecase.ParameterizedSuspendableUseCase
import javax.inject.Inject

class AuthorUsecase @Inject constructor(
    private val interactor: AuthorInteractor
) : ParameterizedSuspendableUseCase<String, Author> {
    override suspend fun invoke(param: String): Author {
        return interactor.get(param)
    }
}
