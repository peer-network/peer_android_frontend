package eu.peernetwork.social.ui.post

import android.content.Context
import eu.peernetwork.core.ui.component.UiComponent

interface Post {
    @javax.inject.Scope
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Scope

    @Scope
    @dagger.Component(
        dependencies = [ Post::class ]
    )
    interface Component : Post

    class Builder(private val dependency: Post) : UiComponent.DefaultBuilder<Post, Component>() {
        override fun build(context: Context): Component {
            TODO("Not yet implemented")
        }
    }
}
