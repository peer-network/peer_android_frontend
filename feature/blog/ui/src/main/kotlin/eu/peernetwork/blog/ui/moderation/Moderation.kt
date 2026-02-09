package eu.peernetwork.blog.ui.moderation

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import eu.peernetwork.blog.ui.provider.BlogProvider
import eu.peernetwork.core.ui.component.UiComponent
import eu.peernetwork.core.ui.component.UiComponentProvider

interface Moderation : BlogProvider {
    @javax.inject.Scope
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Scope

    @Scope
    @dagger.Component(
        dependencies = [Moderation::class],
        modules = [ModerationModule::class]
    )
    interface Component : Moderation, UiComponentProvider {
        fun viewModelFactory(): ViewModelProvider.Factory
    }

    class Builder(private val dependency: Moderation) : UiComponent.DefaultBuilder<Moderation, Component>() {
        override fun build(context: Context): Component {
            return DaggerModeration_Component.builder().moderation(dependency).build()
        }
    }
}