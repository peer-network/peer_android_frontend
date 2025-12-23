package eu.peernetwork.media.ui.selector.explorer

import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import eu.peernetwork.core.ui.annotation.UiBuilder
import eu.peernetwork.core.ui.component.UiComponent
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.factory.UiBuilderFactory
import eu.peernetwork.media.ui.selector.audio.Audio
import eu.peernetwork.media.ui.selector.directory.Directory
import eu.peernetwork.media.ui.selector.photo.Photo
import eu.peernetwork.media.ui.selector.video.Video
import javax.inject.Provider

@Module
object ExplorerModule {
    @Provides
    @Explorer.Scope
    fun provideBuilderFactory(factory: Map<Class<out UiComponent.Builder>,
            @JvmSuppressWildcards Provider<UiComponent.Builder>>): UiComponentProvider.Factory {
        return UiBuilderFactory(factory)
    }

    @Explorer.Scope
    @Provides
    @IntoMap
    @UiBuilder(Photo.Builder::class)
    fun providePhotoBuilder(component: Explorer.Component): UiComponent.Builder {
        return Photo.Builder(component)
    }

    @Explorer.Scope
    @Provides
    @IntoMap
    @UiBuilder(Video.Builder::class)
    fun provideVideoBuilder(component: Explorer.Component): UiComponent.Builder {
        return Video.Builder(component)
    }

    @Explorer.Scope
    @Provides
    @IntoMap
    @UiBuilder(Audio.Builder::class)
    fun provideAudioBuilder(component: Explorer.Component): UiComponent.Builder {
        return Audio.Builder(component)
    }

    @Explorer.Scope
    @Provides
    @IntoMap
    @UiBuilder(Directory.Builder::class)
    fun provideDirectoryBuilder(component: Explorer.Component): UiComponent.Builder {
        return Directory.Builder(component)
    }
}