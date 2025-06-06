package eu.peernetwork.wallet.ui.overview

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import eu.peernetwork.core.common.provider.CoreProvider
import eu.peernetwork.core.ui.component.UiComponent
import eu.peernetwork.wallet.ui.provider.WalletProvider

interface Overview : WalletProvider, CoreProvider {
    @javax.inject.Scope
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Scope

    @Scope
    @dagger.Component(
        dependencies = [ Overview::class ],
        modules = [ OverviewModule::class ]
    )
    interface Component : Overview {
        fun viewModelFactory(): ViewModelProvider.Factory
    }

    class Builder(private val dependency: Overview) : UiComponent.DefaultBuilder<Overview, Component>() {
        override fun build(context: Context): Component {
            return DaggerOverview_Component.builder().overview(dependency).build()
        }
    }
}
