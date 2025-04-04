package eu.peernetwork.app.ui.home

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import eu.peernetwork.app.ui.profile.core.Profile
import eu.peernetwork.core.ui.component.UiComponent
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.persistence.domain.provider.PreferenceProvider
import eu.peernetwork.app.ui.feed.Feed
import eu.peernetwork.blog.ui.point.BlogPoint
import eu.peernetwork.blog.ui.provider.BlogProvider
import eu.peernetwork.user.ui.provider.UserProvider

interface Home : UserProvider, PreferenceProvider, BlogProvider {
    @javax.inject.Scope
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Scope

    @Scope
    @dagger.Component(
        dependencies = [ Home::class ],
        modules = [ HomeModule::class ]
    )
    interface Component : Home, Feed, Profile, UiComponentProvider, BlogPoint {
        fun viewModelFactory(): ViewModelProvider.Factory
    }

    class Builder(private val dependency: Home) : UiComponent.DefaultBuilder<Home, Component>() {
        override fun build(context: Context): Component {
            return DaggerHome_Component.builder().home(dependency).build()
        }
    }
}
