package eu.peernetwork.user.ui.settings.address

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import eu.peernetwork.core.ui.component.UiComponent
import eu.peernetwork.user.ui.provider.UserProvider

interface Address : UserProvider {
    @javax.inject.Scope
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Scope

    @Scope
    @dagger.Component(
        dependencies = [ Address::class ],
        modules = [ AddressModule::class ]
    )
    interface Component : Address {
        fun viewModelFactory(): ViewModelProvider.Factory
    }

    class Builder(private val dependency: Address) : UiComponent.DefaultBuilder<Address, Component>() {
        override fun build(context: Context): Component {
            return DaggerAddress_Component.builder().address(dependency).build()
        }
    }
}
