package eu.peernetwork.blog.ui.provider

import eu.peernetwork.blog.domain.interactor.AuthorInteractor
import eu.peernetwork.blog.domain.interactor.EngagementInteractor

interface InteractorProvider {
    fun authorInteractor(): AuthorInteractor

    fun engagementInteractor(): EngagementInteractor
}
