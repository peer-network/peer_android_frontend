package eu.peernetwork.ads.ui.boost

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import eu.peernetwork.ads.ui.checkout.CheckoutScreen
import eu.peernetwork.ads.ui.quote.QuoteScreen
import eu.peernetwork.core.ui.design.material.DesignRouter

@Composable
fun BoostNavigation(
    id: String,
    controller: NavHostController,
    component: Boost.Component,
    viewModelStoreOwner: ViewModelStoreOwner,
    onDismiss: () -> Unit
) {
    DesignRouter(
        navController = controller,
        startDestination = "quote"
    ) {
        composable("quote") {
            QuoteScreen(
                id = id,
                provider = component,
                viewModelStoreOwner = viewModelStoreOwner,
                onBack = onDismiss
            ) { controller.navigate("checkout") }
        }
        composable("checkout") {
            CheckoutScreen(
                id = id,
                provider = component,
                viewModelStoreOwner = viewModelStoreOwner,
                onBack = { controller.popBackStack() },
                onFinish = onDismiss
            )
        }
    }
}
