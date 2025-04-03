package eu.peernetwork.app.ui.creator

import android.content.Context
import eu.peernetwork.core.ui.component.UiComponent

interface Creator {
    @javax.inject.Scope
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Scope

    @Scope
    @dagger.Component(
        dependencies = [ Creator::class ],
    )
    interface Component : Creator

    class Builder(private val dependency: Creator) : UiComponent.DefaultBuilder<Creator, Component>() {
        override fun build(context: Context): Component {
            return DaggerCreator_Component.builder().creator(dependency).build()
        }
    }
}
