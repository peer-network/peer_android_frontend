package eu.peernetwork.app.ui.profile.preview

import android.content.Context
import eu.peernetwork.blog.ui.post.music.Music
import eu.peernetwork.blog.ui.post.photo.Photo
import eu.peernetwork.blog.ui.post.video.Video
import eu.peernetwork.blog.ui.provider.BlogProvider
import eu.peernetwork.core.ui.component.UiComponent
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.persistence.domain.provider.PreferenceProvider
import eu.peernetwork.user.ui.provider.UserProvider
import eu.peernetwork.user.ui.user.User

interface Preview : UserProvider, PreferenceProvider, BlogProvider {
    @javax.inject.Scope
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Scope

    @Scope
    @dagger.Component(
        dependencies = [ Preview::class ],
        modules = [PreviewModule::class ]
    )
    interface Component : Preview, UiComponentProvider, User, Photo, Video, Music

    class Builder(private val dependency: Preview) : UiComponent.DefaultBuilder<Preview, Component>() {
        override fun build(context: Context): Component {
            return DaggerPreview_Component.builder().preview(dependency).build()
        }
    }
}
