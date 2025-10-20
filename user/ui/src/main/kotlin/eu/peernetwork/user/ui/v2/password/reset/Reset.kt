package eu.peernetwork.user.ui.v2.password.reset

import android.content.Context
import eu.peernetwork.core.ui.component.UiComponent

interface Reset {
    @javax.inject.Scope
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Scope

    @Scope
    @dagger.Component(
        dependencies = [ Reset::class ],
        modules = [ ResetModule::class ]
    )
    interface Component : Reset

    class Builder(private val dependency: Reset) : UiComponent.DefaultBuilder<Reset, Component>() {
        override fun build(context: Context): Component {
            return DaggerReset_Component.builder()
                .reset(dependency)
                .build()
        }
    }
}
