package eu.peernetwork.wallet.ui.transactions

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import eu.peernetwork.core.common.provider.CoreProvider
import eu.peernetwork.core.ui.component.UiComponent
import eu.peernetwork.wallet.ui.provider.WalletProvider
import eu.peernetwork.wallet.ui.transfer.Transfer

interface Transactions : WalletProvider, CoreProvider {
    @javax.inject.Scope
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Scope

    @Scope
    @dagger.Component(
        dependencies = [ Transactions::class ],
        modules = [ TransactionsModule::class ]
    )
    interface Component : Transactions, Transfer {
        fun viewModelFactory(): ViewModelProvider.Factory
    }

    class Builder(private val dependency: Transactions): UiComponent.DefaultBuilder<Transactions, Component>() {
        override fun build(context: Context): Component {
            return DaggerTransactions_Component.builder().transactions(dependency).build()
        }
    }
}
