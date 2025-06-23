package eu.peernetwork.media.ui.selector.explorer

import android.content.Context
import eu.peernetwork.core.ui.component.UiComponent
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.provider.UiProvider
import eu.peernetwork.media.core.interactor.ThumbnailInteractor
import eu.peernetwork.media.ui.camera.Camera
import eu.peernetwork.media.ui.selector.directory.Directory
import eu.peernetwork.media.ui.selector.photo.Photo
import eu.peernetwork.media.ui.selector.video.Video

interface Explorer : UiProvider {
    fun thumbnailInteractor(): ThumbnailInteractor

    @javax.inject.Scope
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Scope

    @Scope
    @dagger.Component(
        dependencies = [ Explorer::class ],
        modules = [ ExplorerModule::class ]
    )
    interface Component : Explorer, Photo, Video, Directory, Camera, UiComponentProvider

    class Builder(private val dependency: Explorer) : UiComponent.DefaultBuilder<Explorer, Component>() {
        override fun build(context: Context): Component {
            return DaggerExplorer_Component.builder().explorer(dependency).build()
        }
    }
}
