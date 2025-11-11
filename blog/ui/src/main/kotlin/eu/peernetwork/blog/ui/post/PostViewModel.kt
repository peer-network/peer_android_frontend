package eu.peernetwork.blog.ui.post

import eu.peernetwork.media.core.interactor.ThumbnailInteractor
import eu.peernetwork.media.core.viewmodel.MediaViewModel
import javax.inject.Inject

class PostViewModel @Inject constructor(
    interactor: ThumbnailInteractor
) : MediaViewModel(interactor)
