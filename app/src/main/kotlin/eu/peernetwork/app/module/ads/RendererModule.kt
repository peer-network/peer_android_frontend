package eu.peernetwork.app.module.ads

import dagger.Module
import dagger.Provides
import eu.peernetwork.ads.ui.checkout.CheckoutBalance
import eu.peernetwork.app.ui.renderer.CheckoutBalanceRenderer

@Module
object RendererModule {
    @Provides
    fun provideCheckoutBalance(renderer: CheckoutBalanceRenderer): CheckoutBalance = renderer
}
