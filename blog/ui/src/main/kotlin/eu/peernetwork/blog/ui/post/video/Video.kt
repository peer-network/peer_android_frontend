package eu.peernetwork.blog.ui.post.video

import android.content.Context
import eu.peernetwork.core.ui.component.UiComponent

interface Video {
    @javax.inject.Scope
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Scope

    @Scope
    @dagger.Component(
        dependencies = [Video::class ]
    )
    interface Component : Video

    class Builder(private val dependency: Video) : UiComponent.DefaultBuilder<Video, Component>() {
        override fun build(context: Context): Component {
            TODO("Not yet implemented")
        }
    }
}
