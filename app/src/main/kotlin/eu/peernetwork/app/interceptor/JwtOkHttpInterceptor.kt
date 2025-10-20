package eu.peernetwork.app.interceptor

import eu.peernetwork.user.domain.usecase.TokenUsecase
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Provider

class JwtOkHttpInterceptor @Inject constructor(
    private val tokenUsecaseProvider: Provider<TokenUsecase>
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        val tokenUsecase = tokenUsecaseProvider.get()
        val token = tokenUsecase()
        val jwt = token?.access

        if (!jwt.isNullOrEmpty()) {
            request = request.newBuilder()
                .addHeader("Authorization", "Bearer $jwt")
                .build()
        }
        return chain.proceed(request)
    }
}
