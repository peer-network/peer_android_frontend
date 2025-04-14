package eu.peernetwork.media.ui.provider

import android.content.Context
import androidx.annotation.OptIn
import androidx.media3.common.C
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.trackselection.DefaultTrackSelector
import eu.peernetwork.media.core.interactor.VideoInteractor
import eu.peernetwork.media.core.provider.VideoProvider
import eu.peernetwork.media.ui.interactor.VideoInteractorDelegate
import javax.inject.Inject

@OptIn(UnstableApi::class)
class VideoProviderDelegate @Inject constructor(
    private val context: Context
) : VideoProvider {
    override val preview: VideoInteractor
        get() = VideoInteractorDelegate(ExoPlayer.Builder(context)
            .setTrackSelector(DefaultTrackSelector(context).apply {
                parameters = buildUponParameters()
                    .setMaxVideoSize(640, 360)
                    .setForceLowestBitrate(true)
                    .setMaxVideoBitrate(1_500_000)
                    .build()
            })
            .build().apply {
                videoScalingMode = C.VIDEO_SCALING_MODE_SCALE_TO_FIT
                repeatMode = Player.REPEAT_MODE_ONE
            })

    override val timeline: VideoInteractor
        get() = VideoInteractorDelegate(ExoPlayer.Builder(context)
            .setTrackSelector(DefaultTrackSelector(context).apply {
                parameters = buildUponParameters()
                    .setMaxVideoSize(640, 360)
                    .setForceLowestBitrate(true)
                    .setMaxVideoBitrate(1_500_000)
                    .build()
            })
            .build().apply {
                volume = 0f
                videoScalingMode = C.VIDEO_SCALING_MODE_SCALE_TO_FIT
                repeatMode = Player.REPEAT_MODE_ONE
            })
}
