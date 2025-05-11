package eu.peernetwork.app.module.blog

import dagger.Binds
import dagger.Module
import eu.peernetwork.blog.data.interactor.AuthorInteractorDelegate
import eu.peernetwork.blog.data.interactor.PointInteractorDelegate
import eu.peernetwork.blog.domain.interactor.AuthorInteractor
import eu.peernetwork.blog.domain.interactor.PointInteractor
import javax.inject.Singleton

@Module(
    includes = [
        ApiModule::class,
        RepositoryModule::class
    ]
)
interface BlogModule {
    @Binds
    fun bindAuthorInteractor(delegate: AuthorInteractorDelegate): AuthorInteractor

    @Binds
    @Singleton
    fun bindEngagementInteractor(delegate: PointInteractorDelegate): PointInteractor
}
