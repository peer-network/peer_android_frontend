package eu.peernetwork.media.ui.attachment

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import eu.peernetwork.core.ui.component.UiComponent
import eu.peernetwork.core.ui.provider.UiProvider
import eu.peernetwork.media.core.interactor.ThumbnailInteractor
import eu.peernetwork.persistence.domain.provider.PreferenceProvider

interface Attachment : PreferenceProvider, UiProvider {
    fun thumbnailInteractor(): ThumbnailInteractor

    @javax.inject.Scope
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Scope

    @Scope
    @dagger.Component(
        dependencies = [ Attachment::class ],
        modules = [ AttachmentModule::class ]
    )
    interface Component : Attachment {
        fun viewModelFactory(): ViewModelProvider.Factory
    }

    class Builder(private val dependency: Attachment) : UiComponent.DefaultBuilder<Attachment, Component>() {
        override fun build(context: Context): Component {
            return DaggerAttachment_Component.builder().attachment(dependency).build()
        }
    }
}
