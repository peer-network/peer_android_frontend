package eu.peernetwork.blog.ui.provider

import eu.peernetwork.blog.domain.interactor.AuthorInteractor
import eu.peernetwork.blog.domain.interactor.PointInteractor

interface InteractorProvider {
    fun authorInteractor(): AuthorInteractor

    fun engagementInteractor(): PointInteractor
}
