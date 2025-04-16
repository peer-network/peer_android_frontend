package eu.peernetwork.app.ui.profile.preview

import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import eu.peernetwork.blog.ui.post.music.Music
import eu.peernetwork.blog.ui.post.photo.Photo
import eu.peernetwork.blog.ui.post.video.Video
import eu.peernetwork.core.ui.annotation.UiBuilder
import eu.peernetwork.core.ui.component.UiComponent
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.factory.UiBuilderFactory
import eu.peernetwork.user.ui.user.User

@Module
object PreviewModule {
    @Provides
    @Preview.Scope
    fun provideBuilderFactory(factory: UiBuilderFactory): UiComponentProvider.Factory = factory

    @Preview.Scope
    @Provides
    @IntoMap
    @UiBuilder(User.Builder::class)
    fun provideUserBuilder(component: Preview.Component): UiComponent.Builder {
        return User.Builder(component)
    }

    @Preview.Scope
    @Provides
    @IntoMap
    @UiBuilder(Photo.Builder::class)
    fun providePhotoBuilder(component: Preview.Component): UiComponent.Builder {
        return Photo.Builder(component)
    }

    @Preview.Scope
    @Provides
    @IntoMap
    @UiBuilder(Video.Builder::class)
    fun provideVideoBuilder(component: Preview.Component): UiComponent.Builder {
        return Video.Builder(component)
    }

    @Preview.Scope
    @Provides
    @IntoMap
    @UiBuilder(Music.Builder::class)
    fun provideMusicBuilder(component: Preview.Component): UiComponent.Builder {
        return Music.Builder(component)
    }
}
