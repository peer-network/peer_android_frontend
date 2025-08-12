package eu.peernetwork.app.interactor

import eu.peernetwork.core.common.interactor.UrlInteractor
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class UrlInteractorDelegate @Inject constructor(
    @Named("baseUrl") private val baseUrl: String
) : UrlInteractor, RemoteInteractor {
    private var url: String = baseUrl

    override fun get(): String = url

    override fun set(url: String) {
        this.url = url
    }
}
