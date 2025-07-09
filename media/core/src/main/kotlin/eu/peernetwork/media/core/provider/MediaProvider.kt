package eu.peernetwork.media.core.provider

import eu.peernetwork.media.core.interactor.ExtractThumbnailsInteractor
import eu.peernetwork.media.core.interactor.ThumbnailInteractor
import eu.peernetwork.media.core.interactor.VideoInteractor

interface MediaProvider : CoreProvider, RendererProvider {
    fun videoInteractor(): VideoInteractor

    fun thumbnailInteractor(): ThumbnailInteractor

    fun extractThumbnailsInteractor(): ExtractThumbnailsInteractor

}
