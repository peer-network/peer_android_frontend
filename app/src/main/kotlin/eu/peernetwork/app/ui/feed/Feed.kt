package eu.peernetwork.app.ui.feed

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import eu.peernetwork.blog.ui.provider.BlogProvider
import eu.peernetwork.blog.ui.timeline.music.Music
import eu.peernetwork.blog.ui.timeline.photo.Photo
import eu.peernetwork.blog.ui.timeline.video.Video
import eu.peernetwork.core.ui.component.UiComponent
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.persistence.domain.provider.PreferenceProvider

interface Feed : PreferenceProvider, BlogProvider {
    @javax.inject.Scope
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Scope

    @Scope
    @dagger.Component(
        dependencies = [ Feed::class ],
        modules = [ FeedModule::class ]
    )
    interface Component : Feed, UiComponentProvider, Music, Video, Photo {
        fun viewModelFactory(): ViewModelProvider.Factory
    }

    class Builder(private val dependency: Feed) : UiComponent.DefaultBuilder<Feed, Component>() {
        override fun build(context: Context): Component {
            return DaggerFeed_Component.builder().feed(dependency).build()
        }
    }
}
