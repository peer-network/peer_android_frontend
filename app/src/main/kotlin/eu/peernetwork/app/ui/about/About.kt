package eu.peernetwork.app.ui.about

import android.content.Context
import eu.peernetwork.app.provider.ApplicationProvider
import eu.peernetwork.core.ui.component.UiComponent

interface About : ApplicationProvider {
    @javax.inject.Scope
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Scope

    @Scope
    @dagger.Component(
        dependencies = [ About::class ]
    )
    interface Component : About

    class Builder(private val dependency: About) : UiComponent.DefaultBuilder<About, Component>() {
        override fun build(context: Context): Component {
            return DaggerAbout_Component.builder().about(dependency).build()
        }
    }
}
