package eu.peernetwork.wallet.ui.transfer

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import eu.peernetwork.core.common.provider.CoreProvider
import eu.peernetwork.core.ui.component.UiComponent
import eu.peernetwork.wallet.ui.provider.WalletProvider

interface Transfer : WalletProvider, CoreProvider {
    @javax.inject.Scope
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Scope

    @Scope
    @dagger.Component(
        dependencies = [ Transfer::class ],
        modules = [ TransferModule::class ]
    )
    interface Component : Transfer {
        fun viewModelFactory(): ViewModelProvider.Factory
    }

    class Builder(private val dependency: Transfer): UiComponent.DefaultBuilder<Transfer, Component>() {
        override fun build(context: Context): Component {
            return DaggerTransfer_Component.builder().transfer(dependency).build()
        }
    }
}