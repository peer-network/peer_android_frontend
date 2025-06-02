package eu.peernetwork.blog.ui.author

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import eu.peernetwork.blog.ui.provider.BlogProvider
import eu.peernetwork.core.ui.component.UiComponent
import eu.peernetwork.media.core.provider.RendererProvider

interface Author : BlogProvider, RendererProvider {
    @javax.inject.Scope
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Scope

    @Scope
    @dagger.Component(
        dependencies = [ Author::class ],
        modules = [ AuthorModule::class ]
    )
    interface Component : Author {
        fun viewModelFactory(): ViewModelProvider.Factory
    }

    class Builder(private val dependency: Author) : UiComponent.DefaultBuilder<Author, Component>() {
        override fun build(context: Context): Component {
            return DaggerAuthor_Component.builder().author(dependency).build()
        }
    }
}
