package eu.peernetwork.social.ui.followings

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import eu.peernetwork.core.ui.component.UiComponent
import eu.peernetwork.social.ui.provider.SocialProvider

interface Followings: SocialProvider{
    @javax.inject.Scope
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Scope

    @Scope
    @dagger.Component(
        dependencies = [Followings::class],
        modules = [FollowingsModule::class]
    )
    interface Component : Followings {
        fun viewModelFactory(): ViewModelProvider.Factory
    }

    class Builder(private val dependency: Followings) : UiComponent.DefaultBuilder<Followings, Component>() {
        override fun build(context: Context): Component {
            return DaggerFollowings_Component.builder().followings(dependency).build()
        }
    }
}