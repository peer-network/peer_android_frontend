package eu.peernetwork.media.ui.selector.video

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import eu.peernetwork.core.ui.component.UiComponent
import eu.peernetwork.core.ui.provider.UiProvider
import eu.peernetwork.media.core.interactor.ThumbnailInteractor

interface Video : UiProvider {
    fun thumbnailInteractor(): ThumbnailInteractor

    @javax.inject.Scope
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Scope

    @Scope
    @dagger.Component(
        dependencies = [ Video::class ],
        modules = [ VideoModule::class ]
    )
    interface Component : Video {
        fun viewModelFactory(): ViewModelProvider.Factory
    }

    class Builder(private val dependency: Video) : UiComponent.DefaultBuilder<Video, Component>() {
        override fun build(context: Context): Component {
            return DaggerVideo_Component.builder().video(dependency).build()
        }
    }
}
