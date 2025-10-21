package eu.peernetwork.user.remote.interceptor

import eu.peernetwork.user.domain.usecase.TokenUsecase
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Provider

class RestJwtInterceptor @Inject constructor(
    private val tokenUsecaseProvider: Provider<TokenUsecase>
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        val tokenUsecase = tokenUsecaseProvider.get()
        val token = tokenUsecase()
        val jwt = token?.access
        if (!jwt.isNullOrEmpty()) {
            request = request.newBuilder()
                .addHeader(HEADER_AUTHORIZATION, String.format(HEADER_BEARER, jwt))
                .build()
        }
        return chain.proceed(request)
    }

    private companion object {
        const val HEADER_AUTHORIZATION = "Authorization"
        const val HEADER_BEARER = "Bearer %s"
    }
}
