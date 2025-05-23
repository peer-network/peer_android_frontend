package eu.peernetwork.app.interactor

import eu.peernetwork.core.common.interactor.UrlInteractor
import javax.inject.Inject
import javax.inject.Named

class UrlInteractorDelegate @Inject constructor(
    @Named("baseUrl") private val baseUrl: String,
    @Named("inviteUrl") private val defaultInviteUrl: String
) : UrlInteractor {
    private var url: String = baseUrl

    private var inviteUrl: String = defaultInviteUrl

    override fun get(): String = url

    override fun invite(): String {
        return inviteUrl
    }

    override fun set(url: String) {
        this.url = url
    }

    override fun invite(url: String) {
        inviteUrl = url
    }
}
