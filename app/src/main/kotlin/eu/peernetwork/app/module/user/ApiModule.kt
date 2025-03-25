package eu.peernetwork.app.module.user

import dagger.Binds
import dagger.Module
import eu.peernetwork.user.data.api.AccountApi
import eu.peernetwork.user.data.api.AuthenticationApi
import eu.peernetwork.user.data.api.ResourceApi
import eu.peernetwork.user.data.api.SearchApi
import eu.peernetwork.user.remote.api.AccountApiDelegate
import eu.peernetwork.user.remote.api.AuthenticationApiDelegate
import eu.peernetwork.user.remote.api.ResourceApiDelegate
import eu.peernetwork.user.remote.api.SearchApiDelegate

@Module
internal interface ApiModule {
    @Binds
    fun bindAccountApi(delegate: AccountApiDelegate): AccountApi

    @Binds
    fun bindAuthenticationApi(delegate: AuthenticationApiDelegate): AuthenticationApi

    @Binds
    fun bindResourceApi(delegate: ResourceApiDelegate): ResourceApi

    @Binds
    fun bindSearchApi(delegate: SearchApiDelegate): SearchApi
}
