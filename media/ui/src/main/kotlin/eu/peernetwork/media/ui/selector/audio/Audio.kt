package eu.peernetwork.media.ui.selector.audio

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import eu.peernetwork.core.ui.component.UiComponent
import eu.peernetwork.core.ui.provider.UiProvider
import eu.peernetwork.media.core.interactor.ThumbnailInteractor

interface Audio : UiProvider {
    fun thumbnailInteractor(): ThumbnailInteractor

    @javax.inject.Scope
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Scope

    @Scope
    @dagger.Component(
        dependencies = [Audio::class],
        modules = [AudioModule::class]
    )
    interface Component : Audio {
        fun viewModelFactory(): ViewModelProvider.Factory
    }

    class Builder(private val dependency: Audio): UiComponent.DefaultBuilder<Audio, Component>() {
        override fun build(context: Context): Component {
            return DaggerAudio_Component.builder().audio(dependency).build()
        }
    }
}