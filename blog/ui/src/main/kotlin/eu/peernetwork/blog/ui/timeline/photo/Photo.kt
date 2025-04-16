package eu.peernetwork.blog.ui.timeline.photo

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import eu.peernetwork.blog.ui.comment.Comment
import eu.peernetwork.blog.ui.engagement.Engagement
import eu.peernetwork.blog.ui.provider.BlogProvider
import eu.peernetwork.core.ui.component.UiComponent
import eu.peernetwork.core.ui.component.UiComponentProvider

interface Photo : BlogProvider {
    @javax.inject.Scope
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Scope

    @Scope
    @dagger.Component(
        dependencies = [Photo::class],
        modules = [PhotoModule::class]
    )
    interface Component : Photo, UiComponentProvider, Engagement {
        fun viewModelFactory(): ViewModelProvider.Factory
    }

    class Builder(private val dependency: Photo) : UiComponent.DefaultBuilder<Photo, Component>() {
        override fun build(context: Context): Component {
            return DaggerPhoto_Component.builder().photo(dependency).build()
        }
    }
}
