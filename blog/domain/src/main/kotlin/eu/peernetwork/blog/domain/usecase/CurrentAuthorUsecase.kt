package eu.peernetwork.blog.domain.usecase

import eu.peernetwork.blog.domain.interactor.AuthorInteractor
import eu.peernetwork.blog.domain.model.Author
import eu.peernetwork.core.common.usecase.SuspendableUseCase
import javax.inject.Inject

class CurrentAuthorUsecase @Inject constructor(
    private val interactor: AuthorInteractor
) : SuspendableUseCase<Author> {
    override suspend fun invoke(): Author {
        return interactor.get()
    }
}
