package eu.peernetwork.media.core.provider

import eu.peernetwork.media.core.interactor.ExtractThumbnailsInteractor
import eu.peernetwork.media.core.interactor.ThumbnailInteractor
import eu.peernetwork.media.core.interactor.VideoInteractor
import eu.peernetwork.media.core.usecase.CroppedPreviewUseCase
import eu.peernetwork.media.core.usecase.VideoTrimUsecase

interface MediaProvider : CoreProvider, RendererProvider {
    fun videoInteractor(): VideoInteractor

    fun thumbnailInteractor(): ThumbnailInteractor

    fun videoTrimUsecase(): VideoTrimUsecase

    fun extractThumbnailsInteractor(): ExtractThumbnailsInteractor

    fun croppedPreviewUseCase(): CroppedPreviewUseCase

}
