package eu.peernetwork.app.ui.version

import android.content.Context
import eu.peernetwork.app.provider.ApplicationProvider
import eu.peernetwork.core.ui.component.UiComponent

interface Version : ApplicationProvider {
    @javax.inject.Scope
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Scope

    @Scope
    @dagger.Component(
        dependencies = [ Version::class ]
    )
    interface Component : Version

    class Builder(private val dependency: Version) : UiComponent.DefaultBuilder<Version, Component>() {
        override fun build(context: Context): Component {
            return DaggerVersion_Component.builder().version(dependency).build()
        }
    }
}
