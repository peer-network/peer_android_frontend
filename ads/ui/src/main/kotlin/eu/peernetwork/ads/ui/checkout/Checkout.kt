package eu.peernetwork.ads.ui.checkout

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import eu.peernetwork.ads.ui.provider.AdsProvider
import eu.peernetwork.core.ui.component.UiComponent

interface Checkout : AdsProvider {
    fun checkoutBalance(): CheckoutBalance

    @javax.inject.Scope
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Scope

    @Scope
    @dagger.Component(
        dependencies = [ Checkout::class ],
        modules = [ CheckoutModule::class ]
    )
    interface Component : Checkout {
        fun viewModelFactory(): ViewModelProvider.Factory
    }

    class Builder(private val dependency: Checkout) : UiComponent.DefaultBuilder<Checkout, Component>() {
        override fun build(context: Context): Component {
            return DaggerCheckout_Component.builder().checkout(dependency).build()
        }
    }
}
