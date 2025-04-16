package eu.peernetwork.app.module.user

import dagger.Binds
import dagger.Module
import eu.peernetwork.user.data.api.AuthenticationApi
import eu.peernetwork.user.data.interactor.AuthenticationInteractorDelegate
import eu.peernetwork.user.data.repository.TokenRepositoryDelegate
import eu.peernetwork.user.domain.interactor.AuthenticationInteractor

@Module(
    includes = [
        ApiModule::class,
        SettingsModule::class,
        RepositoryModule::class,
    ]
)
interface UserModule {
    @Binds
    fun bindAuthenticationListener(delegate: TokenRepositoryDelegate): AuthenticationApi.Listener

    @Binds
    fun bindAuthenticationInteractor(delegate: AuthenticationInteractorDelegate): AuthenticationInteractor
}
