package eu.peernetwork.blog.ui.content.overlay

import eu.peernetwork.media.core.interactor.ThumbnailInteractor
import eu.peernetwork.media.core.viewmodel.MediaViewModel
import javax.inject.Inject

class OverlayViewModel @Inject constructor(
    interactor: ThumbnailInteractor
) : MediaViewModel(interactor)
