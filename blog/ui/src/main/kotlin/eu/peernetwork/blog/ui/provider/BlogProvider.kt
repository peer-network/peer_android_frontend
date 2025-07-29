package eu.peernetwork.blog.ui.provider

import eu.peernetwork.core.common.provider.CoreProvider
import eu.peernetwork.core.ui.usecase.AnnotationUsecase
import eu.peernetwork.media.core.provider.MediaProvider

interface BlogProvider : CoreProvider, MediaProvider, InteractorProvider, RepositoryProvider {
    fun annotationUsecase(): AnnotationUsecase
}
