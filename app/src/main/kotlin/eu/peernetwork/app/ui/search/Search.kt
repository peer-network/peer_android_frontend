package eu.peernetwork.app.ui.search

import android.content.Context
import eu.peernetwork.app.ui.splash.DaggerSplash_Component
import eu.peernetwork.core.ui.component.UiComponent

interface Search {
    @javax.inject.Scope
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Scope

    @Scope
    @dagger.Component(
        dependencies = [ Search::class ],
    )
    interface Component : Search

    class Builder(private val dependency: Search) : UiComponent.DefaultBuilder<Search, Component>() {
        override fun build(context: Context): Component {
            TODO("Not yet implemented")
        }
    }
}
