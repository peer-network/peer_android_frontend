package eu.peernetwork.blog.data.interactor

import eu.peernetwork.blog.domain.interactor.AuthorInteractor
import eu.peernetwork.blog.domain.model.Author
import eu.peernetwork.user.domain.usecase.AuthUserUsecase
import eu.peernetwork.user.domain.usecase.ProfileUsecase
import javax.inject.Inject

class AuthorInteractorDelegate @Inject constructor(
    private val usecase: ProfileUsecase,
    private val authUserUsecase: AuthUserUsecase
) : AuthorInteractor {
    override suspend fun get(): Author {
        val account = authUserUsecase()
        return Author(
            id = account.id,
            slug = account.slug,
            username = account.username,
            imageUrl = account.imageUrl,
            isfollowing = account.isfollowing,
            isfollowed = account.isfollowed
        )
    }

    override suspend fun get(id: String): Author {
        val account = usecase(id)
        return Author(
            id = account.id,
            slug = account.slug,
            username = account.username,
            imageUrl = account.imageUrl,
            isfollowing = account.isfollowing,
            isfollowed = account.isfollowed
        )
    }
}
