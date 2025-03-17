package eu.peernetwork.app.ui.splash

import android.content.Context
import eu.peernetwork.core.ui.component.UiComponent

interface Splash {
    @javax.inject.Scope
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Scope

    @Scope
    @dagger.Component(
        dependencies = [ Splash::class ],
    )
    interface Component : Splash

    class Builder(private val dependency: Splash) : UiComponent.DefaultBuilder<Splash, Component>() {
        override fun build(context: Context): Component {
            return DaggerSplash_Component.builder().splash(dependency).build()
        }
    }
}
