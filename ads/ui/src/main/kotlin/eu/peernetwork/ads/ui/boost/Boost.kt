package eu.peernetwork.ads.ui.boost

import android.content.Context
import eu.peernetwork.ads.ui.checkout.Checkout
import eu.peernetwork.ads.ui.checkout.CheckoutBalance
import eu.peernetwork.ads.ui.descriptor.Descriptor
import eu.peernetwork.ads.ui.provider.AdsProvider
import eu.peernetwork.ads.ui.quote.Quote
import eu.peernetwork.core.ui.component.UiComponent
import eu.peernetwork.core.ui.component.UiComponentProvider

interface Boost : AdsProvider {
    fun checkoutBalance(): CheckoutBalance

    @javax.inject.Scope
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Scope

    @Scope
    @dagger.Component(
        dependencies = [ Boost::class ],
        modules = [ BoostModule::class ]
    )
    interface Component : Boost, UiComponentProvider, Quote, Checkout, Descriptor

    class Builder(private val dependency: Boost) : UiComponent.DefaultBuilder<Boost, Component>() {
        override fun build(context: Context): Component {
            return DaggerBoost_Component.builder().boost(dependency).build()
        }
    }
}
