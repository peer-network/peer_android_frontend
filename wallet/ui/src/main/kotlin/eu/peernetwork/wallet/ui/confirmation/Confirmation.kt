package eu.peernetwork.wallet.ui.confirmation

import android.content.Context
import eu.peernetwork.core.ui.component.UiComponent

interface Confirmation {
    @javax.inject.Scope
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Scope

    @Scope
    @dagger.Component(
        dependencies = [ Confirmation::class ],
        modules = [ ConfirmationModule::class ]
    )
    interface Component : Confirmation

    class Builder(private val dependency: Confirmation): UiComponent.DefaultBuilder<Confirmation, Component>() {
        override fun build(context: Context): Component {
            return DaggerConfirmation_Component.builder().confirmation(dependency).build()
        }
    }
}
