package eu.peernetwork.ads.ui.checkout

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.extension.builder

@Composable
fun CheckoutScreen(
    id: String,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    onBack: () -> Unit,
    onFinish: () -> Unit
) {
    val context = LocalContext.current
    val component = remember { provider.builder(Checkout.Builder::class.java).build(context) }
    val viewModel = viewModel(
        modelClass = CheckoutViewModel::class.java,
        viewModelStoreOwner = viewModelStoreOwner,
        factory = component.viewModelFactory()
    )
    val state by viewModel.state.collectAsStateWithLifecycle()
    val isLoading = remember { derivedStateOf { state is CheckoutViewModel.State.Loading } }
    val isSuccess = remember { derivedStateOf { state is CheckoutViewModel.State.Success } }
    val handleFinish by rememberUpdatedState(onFinish)
    CheckoutPage(
        isLoading = isLoading,
        onBack = onBack
    ) {
        viewModel.invoke(id)
    }
    LaunchedEffect(isSuccess.value) {
        if (isSuccess.value) {
            handleFinish()
        }
    }
}
