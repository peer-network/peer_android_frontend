package eu.peernetwork.ads.ui.checkout

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.lifecycle.ViewModelStoreOwner
import eu.peernetwork.core.ui.design.luna.DesignStreamState
import eu.peernetwork.core.ui.renderer.Renderer

interface CheckoutBalance : Renderer.Stateless {
    @Composable
    fun Charges(
        viewModelStoreOwner: ViewModelStoreOwner,
        content: @Composable (State<DesignStreamState<Double>>
    ) -> Unit)
}
