package eu.peernetwork.blog.ui.creator

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import eu.peernetwork.blog.ui.author.Author
import eu.peernetwork.blog.ui.provider.BlogProvider
import eu.peernetwork.core.ui.component.UiComponent
import eu.peernetwork.core.ui.component.UiComponentProvider

interface Creator : BlogProvider {
    @javax.inject.Scope
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Scope

    @Scope
    @dagger.Component(
        dependencies = [ Creator::class ],
        modules = [ CreatorModule::class ]
    )
    interface Component : Creator, Author, UiComponentProvider {
        fun viewModelFactory(): ViewModelProvider.Factory
    }

    class Builder(private val dependency: Creator) : UiComponent.DefaultBuilder<Creator, Component>() {
        override fun build(context: Context): Component {
            return DaggerCreator_Component.builder().creator(dependency).creatorModule(CreatorModule(context)).build()
        }
    }
}
