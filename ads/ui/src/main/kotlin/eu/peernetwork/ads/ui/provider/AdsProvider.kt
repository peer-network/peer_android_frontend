package eu.peernetwork.ads.ui.provider

import eu.peernetwork.ads.ui.checkout.CheckoutBalance
import eu.peernetwork.core.common.provider.CoreProvider
import eu.peernetwork.media.core.provider.MediaProvider

interface AdsProvider : CoreProvider, RepositoryProvider, MediaProvider {
    fun checkoutBalance(): CheckoutBalance
}
