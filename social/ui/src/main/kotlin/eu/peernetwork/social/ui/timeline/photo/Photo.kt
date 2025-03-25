package eu.peernetwork.social.ui.timeline.photo

import android.content.Context
import eu.peernetwork.core.ui.component.UiComponent

interface Photo {
    @javax.inject.Scope
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Scope

    @Scope
    @dagger.Component(
        dependencies = [Photo::class ]
    )
    interface Component : Photo

    class Builder(private val dependency: Photo) : UiComponent.DefaultBuilder<Photo, Component>() {
        override fun build(context: Context): Component {
            TODO("Not yet implemented")
        }
    }
}
