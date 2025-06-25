package eu.peernetwork.app.ui.feed

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import eu.peernetwork.app.provider.ApplicationProvider
import eu.peernetwork.app.ui.profile.Profile
import eu.peernetwork.app.ui.search.Search
import eu.peernetwork.blog.ui.timeline.photo.Photo
import eu.peernetwork.blog.ui.timeline.video.Video
import eu.peernetwork.core.ui.component.UiComponent
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.social.ui.connection.Connection
import eu.peernetwork.wallet.ui.confirmation.Confirmation

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
        Video,
        Photo,
        Search,
        Profile,
        Connection,
        Confirmation {
        fun viewModelFactory(): ViewModelProvider.Factory
    }

    class Builder(private val dependency: Feed) : UiComponent.DefaultBuilder<Feed, Component>() {
        override fun build(context: Context): Component {
            return DaggerFeed_Component.builder().feed(dependency).build()
        }
    }
}
