package eu.peernetwork.media.ui.thumbnail

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import eu.peernetwork.core.ui.component.UiComponent
import eu.peernetwork.core.ui.provider.UiProvider

interface Thumbnail : UiProvider {
    @javax.inject.Scope
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Scope

    @Scope
    @dagger.Component(
        dependencies = [ Thumbnail::class ],
        modules = [ ThumbnailModule::class ]
    )
    interface Component : Thumbnail {
        fun viewModelFactory(): ViewModelProvider.Factory
    }

    class Builder(private val dependency: Thumbnail) : UiComponent.DefaultBuilder<Thumbnail, Component>() {
        override fun build(context: Context): Component {
            return DaggerThumbnail_Component.builder().thumbnail(dependency).build()
        }
    }
}
