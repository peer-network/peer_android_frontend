package eu.peernetwork.blog.ui.provider

import eu.peernetwork.blog.ui.post.PostUserConnection
import eu.peernetwork.core.common.provider.CoreProvider
import eu.peernetwork.media.core.provider.MediaProvider

interface BlogProvider : CoreProvider,
    MediaProvider,
    InteractorProvider,
    RepositoryProvider {
    fun postUserFollow(): PostUserConnection
}
