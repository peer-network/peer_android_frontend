package eu.peernetwork.app.module.blog

import dagger.Binds
import dagger.Module
import eu.peernetwork.blog.data.interactor.AuthorInteractorDelegate
import eu.peernetwork.blog.domain.interactor.AuthorInteractor

@Module(
    includes = [ ApiModule::class, RepositoryModule::class ]
)
interface BlogModule {
    @Binds
    fun bindAuthorInteractor(delegate: AuthorInteractorDelegate): AuthorInteractor
}
