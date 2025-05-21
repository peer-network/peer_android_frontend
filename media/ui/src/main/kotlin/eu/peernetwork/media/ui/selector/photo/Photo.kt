package eu.peernetwork.media.ui.selector.photo

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import eu.peernetwork.core.ui.component.UiComponent
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.provider.UiProvider
import eu.peernetwork.media.ui.thumbnail.Thumbnail

interface Photo : UiProvider {
    @javax.inject.Scope
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Scope

    @Scope
    @dagger.Component(
        dependencies = [ Photo::class ],
        modules = [ PhotoModule::class ]
    )
    interface Component : Photo, Thumbnail, UiComponentProvider {
        fun viewModelFactory(): ViewModelProvider.Factory
    }

    class Builder(private val dependency: Photo) : UiComponent.DefaultBuilder<Photo, Component>() {
        override fun build(context: Context): Component {
            return DaggerPhoto_Component.builder().photo(dependency).build()
        }
    }
}
