package eu.peernetwork.app.provider

import javax.inject.Inject
import javax.inject.Named

interface UrlProvider {
    fun get(): String

    fun baseUrl(url: String)

    class Delegate @Inject constructor(
        @Named("baseUrl") private val baseUrl: String
    ) : UrlProvider {
        private var url: String = baseUrl

        override fun get(): String = url

        override fun baseUrl(url: String) {
            this.url = url
        }
    }
}
