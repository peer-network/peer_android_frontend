package eu.peernetwork.app.module.user

import com.apollographql.apollo3.ApolloClient
import dagger.Module
import dagger.Provides
import eu.peernetwork.app.interceptor.LoggingInterceptor
import eu.peernetwork.core.common.interactor.UrlInteractor
import eu.peernetwork.user.data.api.AccountApi
import eu.peernetwork.user.data.api.AuthenticationApi
import eu.peernetwork.user.data.api.ResourceApi
import eu.peernetwork.user.data.api.SearchApi
import eu.peernetwork.user.data.api.TokenApi
import eu.peernetwork.user.remote.api.AccountApiDelegate
import eu.peernetwork.user.remote.api.AuthenticationApiDelegate
import eu.peernetwork.user.remote.api.ResourceApiDelegate
import eu.peernetwork.user.remote.api.SearchApiDelegate
import eu.peernetwork.user.remote.api.TokenApiDelegate
import eu.peernetwork.user.remote.usecase.JwtExpiryUsecase

@Module
internal object ApiModule {
    @Provides
    fun providesAccountApi(delegate: AccountApiDelegate): AccountApi = delegate

    @Provides
    fun providesAuthenticationApi(delegate: AuthenticationApiDelegate): AuthenticationApi = delegate

    @Provides
    fun providesResourceApi(delegate: ResourceApiDelegate): ResourceApi = delegate

    @Provides
    fun providesSearchApi(delegate: SearchApiDelegate): SearchApi = delegate

    @Provides
    fun providesTokenApi(
        provider: UrlInteractor,
        logger: LoggingInterceptor,
        usecase: JwtExpiryUsecase
    ): TokenApi = TokenApiDelegate(
        ApolloClient.Builder()
            .serverUrl("${provider.get()}/graphql")
            .addInterceptor(logger).build(),
        usecase
    )
}
