package eu.peernetwork.app.module.media

import android.content.Context
import android.media.MediaPlayer
import androidx.annotation.OptIn
import androidx.media3.common.C
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.DefaultLoadControl
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.trackselection.DefaultTrackSelector
import dagger.Module
import dagger.Provides
import eu.peernetwork.media.core.renderer.AudioPlayer
import eu.peernetwork.media.core.renderer.ImageView
import eu.peernetwork.media.core.renderer.MediaController
import eu.peernetwork.media.core.renderer.VideoPlayer
import eu.peernetwork.media.core.renderer.VideoThumbnail
import eu.peernetwork.media.ui.annotation.Screen
import eu.peernetwork.media.ui.annotation.Timeline
import eu.peernetwork.media.ui.renderer.AudioPlayerDelegate
import eu.peernetwork.media.ui.renderer.ImageViewDelegate
import eu.peernetwork.media.ui.renderer.MediaControllerDelegate
import eu.peernetwork.media.ui.renderer.VideoPlayerDelegate
import eu.peernetwork.media.ui.renderer.VideoThumbnailDelegate
import javax.inject.Singleton

@Module(includes = [
    UsecaseModule::class,
    InteractorModule::class
])
object MediaModule {
    @Provides
    @Singleton
    @Screen
    fun provideMediaPlayer(): MediaPlayer = MediaPlayer()

    @Provides
    @Singleton
    @Timeline
    fun provideTimelineMediaPlayer(): MediaPlayer = MediaPlayer()

    @Provides
    @Singleton
    @OptIn(UnstableApi::class)
    fun providesExoPlayer(context: Context): ExoPlayer {
        return ExoPlayer.Builder(context)
            .setTrackSelector(DefaultTrackSelector(context).apply {
                parameters = buildUponParameters()
                    .setMaxVideoSize(640, 360)
                    .setForceLowestBitrate(true)
                    .setMaxVideoBitrate(1_500_000)
                    .build()
            }).setLoadControl(DefaultLoadControl.Builder()
                .setBufferDurationsMs(
                    1500,
                    5000,
                    500,
                    1000
                ).build()).build().apply {
                videoScalingMode = C.VIDEO_SCALING_MODE_SCALE_TO_FIT
                repeatMode = Player.REPEAT_MODE_ALL
            }
    }

    @Provides
    @Singleton
    fun bindImageView(delegate: ImageViewDelegate): ImageView = delegate

    @Provides
    @Singleton
    fun bindVideoThumbnail(delegate: VideoThumbnailDelegate): VideoThumbnail = delegate

    @Provides
    @Singleton
    fun bindVideoPlayer(delegate: VideoPlayerDelegate): VideoPlayer = delegate

    @Provides
    @Singleton
    fun bindAudioPlayer(delegate: AudioPlayerDelegate): AudioPlayer = delegate

    @Provides
    @Singleton
    fun provideMediaController(delegate: MediaControllerDelegate): MediaController = delegate
}