package eu.peernetwork.wallet.ui.balance

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import eu.peernetwork.core.common.provider.CoreProvider
import eu.peernetwork.core.ui.component.UiComponent
import eu.peernetwork.wallet.ui.provider.WalletProvider

interface Balance : WalletProvider, CoreProvider {
    @javax.inject.Scope
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Scope

    @Scope
    @dagger.Component(
        dependencies = [ Balance::class ],
        modules = [ BalanceModule::class ]
    )
    interface Component : Balance {
        fun viewModelFactory(): ViewModelProvider.Factory
    }

    class Builder(private val dependency: Balance) : UiComponent.DefaultBuilder<Balance, Component>() {
        override fun build(context: Context): Component {
            return DaggerBalance_Component.builder().balance(dependency).build()
        }
    }
}
