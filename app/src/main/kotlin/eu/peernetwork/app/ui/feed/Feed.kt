package eu.peernetwork.app.ui.feed

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import eu.peernetwork.app.provider.ApplicationProvider
import eu.peernetwork.app.ui.profile.Profile
import eu.peernetwork.blog.ui.timeline.music.Music
import eu.peernetwork.blog.ui.timeline.photo.Photo
import eu.peernetwork.blog.ui.timeline.video.Video
import eu.peernetwork.core.ui.component.UiComponent
import eu.peernetwork.core.ui.component.UiComponentProvider
interface Feed : ApplicationProvider {
    @javax.inject.Scope
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Scope

    @Scope
    @dagger.Component(
        dependencies = [ Feed::class ],
        modules = [ FeedModule::class ]
    )
    interface Component : Feed,
        UiComponentProvider,
        Music,
        Video,
        Photo,
        Profile {
        fun viewModelFactory(): ViewModelProvider.Factory
    }

    class Builder(private val dependency: Feed) : UiComponent.DefaultBuilder<Feed, Component>() {
        override fun build(context: Context): Component {
            return DaggerFeed_Component.builder().feed(dependency).build()
        }
    }
}
