package eu.peernetwork.media.ui.selector.directory

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import eu.peernetwork.core.ui.component.UiComponent
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.provider.UiProvider
import eu.peernetwork.media.ui.thumbnail.Thumbnail

interface Directory : UiProvider {

    @javax.inject.Scope
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Scope

    @Scope
    @dagger.Component(
        dependencies = [ Directory::class ],
        modules = [ DirectoryModule::class ]
    )
    interface Component : Directory, Thumbnail, UiComponentProvider {
        fun viewModelFactory(): ViewModelProvider.Factory
    }

    class Builder(private val dependency: Directory) : UiComponent.DefaultBuilder<Directory, Component>() {
        override fun build(context: Context): Component {
            return DaggerDirectory_Component.builder().directory(dependency).build()
        }
    }
}
