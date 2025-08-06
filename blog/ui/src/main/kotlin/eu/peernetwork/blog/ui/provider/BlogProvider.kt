package eu.peernetwork.blog.ui.provider

import eu.peernetwork.blog.ui.interactor.BlogInteractor
import eu.peernetwork.core.common.provider.CoreProvider
import eu.peernetwork.media.core.provider.MediaProvider

interface BlogProvider : CoreProvider,
    MediaProvider,
    InteractorProvider,
    RepositoryProvider {
    fun blogInteractor(): BlogInteractor
}
