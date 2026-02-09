package eu.peernetwork.ads.ui.checkout

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import eu.peernetwork.ads.domain.model.Ads
import eu.peernetwork.ads.domain.model.Description
import eu.peernetwork.core.ui.R
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.luna.DesignStream
import eu.peernetwork.core.ui.extension.builder

@Composable
fun CheckoutScreen(
    id: String,
    description: Description,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    onBack: () -> Unit,
    onProfile: () -> Unit,
    onFinish: () -> Unit
) {
    val context = LocalContext.current
    val component = remember {
        provider.builder(Checkout.Builder::class.java).build(context)
    }
    val viewModel = viewModel(
        modelClass = CheckoutViewModel::class.java,
        viewModelStoreOwner = viewModelStoreOwner,
        factory = component.viewModelFactory()
    )
    val state by viewModel.state.collectAsStateWithLifecycle()
    val isLoading = remember { derivedStateOf { state is CheckoutViewModel.State.Loading } }
    val isSuccess = remember { derivedStateOf { state is CheckoutViewModel.State.Success } }
    val order = remember { derivedStateOf { (state as? CheckoutViewModel.State.Success?)?.order } }
    val showDialog = remember(isSuccess.value) { mutableStateOf(isSuccess.value) }
    val errorMessage = stringResource(R.string.unknown_error_message)
    val error = remember { derivedStateOf {
        (state as? CheckoutViewModel.State.Error)?.error?.let { error ->
            error.message?.let {
                component.resource().string(it)
            } ?: errorMessage
        }
    } }
    val handleFinish by rememberUpdatedState(onFinish)
    val handleProfile by rememberUpdatedState(onProfile)
    component.checkoutBalance().Rate(
        viewModelStoreOwner = viewModelStoreOwner
    ) { tax, refresh ->
        val price = remember { derivedStateOf {
            description.plans.first { it is Ads.Plan.Pinned } as Ads.Plan.Pinned
        } }
        DesignStream(
            state = tax,
            loading = { CheckoutSkeleton() },
            error = { error ->
                CheckoutError(
                    error = error,
                    component = component
                ) { refresh() }
            },
        ) { charges ->
            CheckoutPage(
                charge = charges.value,
                price = price.value.price,
                isLoading = isLoading,
                error = error,
                onBack = onBack,
                onPay = { viewModel.invoke(id) }
            ) { component.checkoutBalance()(modifier = Modifier) }
        }
    }
    CheckoutModal(
        state = order,
        showDialog = showDialog,
        onConfirm = {
            showDialog.value = false
            handleFinish()
        },
        onProfile = {
            showDialog.value = false
            handleProfile()
        }
    ) {
        showDialog.value = false
        viewModel.reset()
    }
    DisposableEffect(Unit) {
        onDispose { viewModel.reset() }
    }
}
