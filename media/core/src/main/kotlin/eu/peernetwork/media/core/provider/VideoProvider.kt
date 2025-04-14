package eu.peernetwork.media.core.provider

import eu.peernetwork.media.core.interactor.VideoInteractor

interface VideoProvider {
    val preview: VideoInteractor
    val timeline: VideoInteractor
}
