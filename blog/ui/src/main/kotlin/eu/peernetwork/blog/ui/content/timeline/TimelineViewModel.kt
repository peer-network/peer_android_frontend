package eu.peernetwork.blog.ui.content.timeline

import eu.peernetwork.media.core.interactor.ThumbnailInteractor
import eu.peernetwork.media.core.viewmodel.MediaViewModel
import javax.inject.Inject

class TimelineViewModel @Inject constructor(
    interactor: ThumbnailInteractor
) : MediaViewModel(interactor)
