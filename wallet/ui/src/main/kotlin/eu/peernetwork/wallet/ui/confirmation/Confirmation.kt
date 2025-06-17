package eu.peernetwork.wallet.ui.confirmation

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import eu.peernetwork.core.common.provider.CoreProvider
import eu.peernetwork.core.ui.component.UiComponent
import eu.peernetwork.wallet.ui.provider.WalletProvider

interface Confirmation : WalletProvider, CoreProvider {
    @javax.inject.Scope
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Scope

    @Scope
    @dagger.Component(
        dependencies = [ Confirmation::class ],
        modules = [ ConfirmationModule::class ]
    )
    interface Component : Confirmation {
        fun viewModelFactory(): ViewModelProvider.Factory
    }

    class Builder(private val dependency: Confirmation): UiComponent.DefaultBuilder<Confirmation, Component>() {
        override fun build(context: Context): Component {
            return DaggerConfirmation_Component.builder().confirmation(dependency).build()
        }
    }
}
