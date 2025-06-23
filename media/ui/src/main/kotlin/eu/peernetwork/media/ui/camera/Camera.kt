package eu.peernetwork.media.ui.camera

import android.content.Context
import eu.peernetwork.core.ui.component.UiComponent
import eu.peernetwork.core.ui.provider.UiProvider
import eu.peernetwork.media.core.interactor.ThumbnailInteractor

interface Camera : UiProvider {
    fun thumbnailInteractor(): ThumbnailInteractor

    @javax.inject.Scope
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Scope

    @Scope
    @dagger.Component(
        dependencies = [Camera::class],
        modules = [CameraModule::class]
    )
    interface Component : Camera

    class Builder(private val dependency: Camera) : UiComponent.DefaultBuilder<Camera, Component>() {
        override fun build(context: Context): Component {
            return DaggerCamera_Component.builder().camera(dependency).build()
        }
    }
}
