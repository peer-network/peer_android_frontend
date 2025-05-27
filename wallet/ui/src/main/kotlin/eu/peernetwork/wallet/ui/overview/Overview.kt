package eu.peernetwork.wallet.ui.overview

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import eu.peernetwork.core.ui.component.UiComponent
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.wallet.ui.provider.WalletProvider
import eu.peernetwork.wallet.ui.transfer.Transfer

interface Overview : WalletProvider {
    @javax.inject.Scope
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Scope

    @Scope
    @dagger.Component(
        dependencies = [ Overview::class ],
        modules = [ OverviewModule::class ]
    )
    interface Component : Overview, Transfer, UiComponentProvider {
        fun viewModelFactory(): ViewModelProvider.Factory
    }

    class Builder(private val dependency: Overview) : UiComponent.DefaultBuilder<Overview, Component>() {
        override fun build(context: Context): Component {
            return DaggerOverview_Component.builder().overview(dependency).build()
        }
    }
}
