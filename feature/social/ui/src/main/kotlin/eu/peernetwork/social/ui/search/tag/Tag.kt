package eu.peernetwork.social.ui.search.tag

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import eu.peernetwork.core.ui.component.UiComponent
import eu.peernetwork.social.ui.provider.SocialProvider

interface Tag : SocialProvider {
    @javax.inject.Scope
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Scope

    @Scope
    @dagger.Component(
        dependencies = [ Tag::class ],
        modules = [ TagModule::class ]
    )
    interface Component : Tag {
        fun viewModelFactory(): ViewModelProvider.Factory
    }

    class Builder(private val dependency: Tag) : UiComponent.DefaultBuilder<Tag, Component>() {
        override fun build(context: Context): Component {
            return DaggerTag_Component.builder().tag(dependency).build()
        }
    }
}
