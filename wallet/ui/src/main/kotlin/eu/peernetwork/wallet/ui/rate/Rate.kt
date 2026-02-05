package eu.peernetwork.wallet.ui.rate

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import eu.peernetwork.core.common.provider.CoreProvider
import eu.peernetwork.core.ui.component.UiComponent
import eu.peernetwork.wallet.ui.provider.WalletProvider
import eu.peernetwork.wallet.ui.transfer.Transfer

interface Rate : WalletProvider, CoreProvider {
    @javax.inject.Scope
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Scope

    @Scope
    @dagger.Component(
        dependencies = [ Rate::class ],
        modules = [ RateModule::class ]
    )
    interface Component : Rate, Transfer {
        fun viewModelFactory(): ViewModelProvider.Factory
    }

    class Builder(private val dependency: Rate): UiComponent.DefaultBuilder<Rate, Component>() {
        override fun build(context: Context): Component {
            return DaggerRate_Component.builder().rate(dependency).build()
        }
    }
}
