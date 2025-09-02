package eu.peernetwork.app.module.media

import android.content.Context
import dagger.Module
import dagger.Provides
import eu.peernetwork.media.core.annotation.DiskCache
import eu.peernetwork.media.core.annotation.MemoryCache
import eu.peernetwork.media.core.interactor.ThumbnailInteractor
import eu.peernetwork.media.core.interactor.VideoInteractor
import eu.peernetwork.media.ui.core.MediaSession
import eu.peernetwork.media.ui.interactor.BitmapInteractor
import eu.peernetwork.media.ui.interactor.DiskCacheInteractor
import eu.peernetwork.media.ui.interactor.MemoryCacheInteractor
import eu.peernetwork.media.ui.interactor.ThumbnailInteractorDelegate
import eu.peernetwork.media.ui.interactor.VideoInteractorDelegate
import javax.inject.Singleton

@Module
object InteractorModule {
    @Provides
    @Singleton
    @MemoryCache
    fun bindMemoryCacheInteractor(): BitmapInteractor {
        return MemoryCacheInteractor((Runtime.getRuntime().maxMemory() / 8).toInt())
    }

    @Provides
    @DiskCache
    fun bindDiskCacheInteractor(context: Context): BitmapInteractor {
        return DiskCacheInteractor(context, "peer_bitmap_cache")
    }

    @Provides
    fun bindBitmapInteractor(@MemoryCache delegate: BitmapInteractor): BitmapInteractor.BitmapAdapter {
        return delegate as BitmapInteractor.BitmapAdapter
    }

    @Provides
    fun bindThumbnailInteractor(delegate: ThumbnailInteractorDelegate): ThumbnailInteractor = delegate

    @Provides
    fun bindVideoInteractor(delegate: VideoInteractorDelegate): VideoInteractor = delegate

    @Provides
    fun bindMediaSession(delegate: VideoInteractorDelegate): MediaSession = delegate
}
