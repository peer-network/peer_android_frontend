package eu.peernetwork.app.module.user

import dagger.Binds
import dagger.Module
import eu.peernetwork.user.data.api.AuthenticationApi
import eu.peernetwork.user.data.repository.AccountRepositoryDelegate
import eu.peernetwork.user.data.repository.AuthenticationRepositoryDelegate
import eu.peernetwork.user.data.repository.SearchRepositoryDelegate
import eu.peernetwork.user.data.repository.TokenRepositoryDelegate
import eu.peernetwork.user.domain.repository.AccountRepository
import eu.peernetwork.user.domain.repository.AuthenticationRepository
import eu.peernetwork.user.domain.repository.SearchRepository
import eu.peernetwork.user.domain.repository.TokenRepository

@Module(
    includes = [
        ApiModule::class,
        SettingsModule::class
    ]
)
interface UserModule {
    @Binds
    fun bindAccountRepository(delegate: AccountRepositoryDelegate): AccountRepository

    @Binds
    fun bindAuthenticationRepository(delegate: AuthenticationRepositoryDelegate): AuthenticationRepository

    @Binds
    fun bindSearchRepository(delegate: SearchRepositoryDelegate): SearchRepository

    @Binds
    fun bindTokenRepository(delegate: TokenRepositoryDelegate): TokenRepository

    @Binds
    fun bindAuthenticationListener(delegate: TokenRepositoryDelegate): AuthenticationApi.Listener
}
