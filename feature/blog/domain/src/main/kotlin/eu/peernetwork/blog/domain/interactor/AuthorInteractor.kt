package eu.peernetwork.blog.domain.interactor

import eu.peernetwork.blog.domain.model.Author

interface AuthorInteractor {
    suspend fun get(): Author

    suspend fun get(id: String): Author
}
