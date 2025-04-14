package eu.peernetwork.blog.ui.post.music

import android.content.Context
import eu.peernetwork.core.ui.component.UiComponent

interface Music {
    @javax.inject.Scope
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Scope

    @Scope
    @dagger.Component(
        dependencies = [Music::class ]
    )
    interface Component : Music

    class Builder(private val dependency: Music) : UiComponent.DefaultBuilder<Music, Component>() {
        override fun build(context: Context): Component {
            TODO("Not yet implemented")
        }
    }
}
