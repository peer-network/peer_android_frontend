package eu.peernetwork.app.module.social

import dagger.Binds
import dagger.Module
import eu.peernetwork.social.data.interactor.SearchInteractorDelegate
import eu.peernetwork.social.domain.interactor.SearchInteractor

@Module(includes = [
    ApiModule::class,
    RepositoryModule::class,
])
interface SocialModule {
    @Binds
    fun bindSearchInteractor(delegate: SearchInteractorDelegate): SearchInteractor
}
