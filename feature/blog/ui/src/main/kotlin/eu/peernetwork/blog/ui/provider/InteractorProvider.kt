package eu.peernetwork.blog.ui.provider

import eu.peernetwork.blog.domain.interactor.AuthorInteractor
import eu.peernetwork.blog.domain.interactor.CommentInteractor
import eu.peernetwork.blog.domain.interactor.ContentInteractor
import eu.peernetwork.blog.domain.interactor.EngagementInteractor
import eu.peernetwork.core.common.interactor.SessionInteractor

interface InteractorProvider {
    fun authorInteractor(): AuthorInteractor

    fun engagementInteractor(): EngagementInteractor

    fun contentInteractor(): ContentInteractor

    fun commentInteractor(): CommentInteractor

    fun sessionInteractor(): SessionInteractor
}
