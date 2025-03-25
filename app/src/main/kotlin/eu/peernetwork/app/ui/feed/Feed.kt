package eu.peernetwork.app.ui.feed

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import eu.peernetwork.core.ui.component.UiComponent
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.persistence.domain.provider.PreferenceProvider
import eu.peernetwork.social.ui.timeline.music.Music
import eu.peernetwork.social.ui.timeline.photo.Photo
import eu.peernetwork.social.ui.timeline.video.Video

interface Feed : PreferenceProvider {
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
