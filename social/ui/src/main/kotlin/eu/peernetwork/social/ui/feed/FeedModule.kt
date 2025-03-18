package eu.peernetwork.social.ui.feed

import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import eu.peernetwork.core.ui.annotation.UiBuilder
import eu.peernetwork.core.ui.component.UiComponent
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.factory.UiBuilderFactory
import eu.peernetwork.social.ui.content.music.Music
import eu.peernetwork.social.ui.content.photo.Photo
import eu.peernetwork.social.ui.content.video.Video

@Module
object FeedModule {
    @Provides
    @Feed.Scope
    fun provideBuilderFactory(factory: UiBuilderFactory): UiComponentProvider.Factory = factory

    @Feed.Scope
    @Provides
    @IntoMap
    @UiBuilder(Music.Builder::class)
    fun provideMusicBuilder(component: Feed.Component): UiComponent.Builder {
        return Music.Builder(component)
    }

    @Feed.Scope
    @Provides
    @IntoMap
    @UiBuilder(Photo.Builder::class)
    fun providePhotoBuilder(component: Feed.Component): UiComponent.Builder {
        return Photo.Builder(component)
    }

    @Feed.Scope
    @Provides
    @IntoMap
    @UiBuilder(Video.Builder::class)
    fun provideVideoBuilder(component: Feed.Component): UiComponent.Builder {
        return Video.Builder(component)
    }
}
