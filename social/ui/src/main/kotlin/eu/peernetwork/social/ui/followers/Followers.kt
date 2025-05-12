package eu.peernetwork.social.ui.followers

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import eu.peernetwork.core.ui.component.UiComponent
import eu.peernetwork.social.ui.provider.SocialProvider

interface Followers: SocialProvider{
    @javax.inject.Scope
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Scope

    @Scope
    @dagger.Component(
        dependencies = [Followers::class],
        modules = [FollowersModule::class]
    )
    interface Component : Followers {
        fun viewModelFactory(): ViewModelProvider.Factory
    }

    class Builder(private val dependency: Followers) : UiComponent.DefaultBuilder<Followers, Component>() {
        override fun build(context: Context): Component {
            return DaggerFollowers_Component.builder().followers(dependency).build()
        }
    }
}