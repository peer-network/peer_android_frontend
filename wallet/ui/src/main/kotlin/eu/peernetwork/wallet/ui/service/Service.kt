package eu.peernetwork.wallet.ui.service

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import eu.peernetwork.core.common.provider.CoreProvider
import eu.peernetwork.core.ui.component.UiComponent
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.wallet.ui.provider.WalletProvider
import eu.peernetwork.wallet.ui.transfer.Transfer

interface Service : WalletProvider, CoreProvider {
    @javax.inject.Scope
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Scope

    @Scope
    @dagger.Component(
        dependencies = [ Service::class ],
        modules = [ ServiceModule::class ]
    )
    interface Component : Service, Transfer, UiComponentProvider {
        fun viewModelFactory(): ViewModelProvider.Factory
    }

    class Builder(private val dependency: Service): UiComponent.DefaultBuilder<Service, Component>() {
        override fun build(context: Context): Component {
            return DaggerService_Component.builder().service(dependency).build()
        }
    }
}
