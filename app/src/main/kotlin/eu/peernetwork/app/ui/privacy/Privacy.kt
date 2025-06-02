package eu.peernetwork.app.ui.privacy

import android.content.Context
import eu.peernetwork.core.ui.component.UiComponent

interface Privacy {
    @javax.inject.Scope
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Scope

    @Scope
    @dagger.Component(
        dependencies = [ Privacy::class ],
        modules = [ PrivacyModule::class ]
    )
    interface Component : Privacy

    class Builder(private val dependency: Privacy) : UiComponent.DefaultBuilder<Privacy, Component>() {
        override fun build(context: Context): Component {
            return DaggerPrivacy_Component.builder().privacy(dependency).build()
        }
    }
}
