package eu.peernetwork.wallet.ui.transfer.v2

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.luna.DesignStream
import eu.peernetwork.core.ui.design.luna.DesignStreamState
import eu.peernetwork.core.ui.extension.builder
import eu.peernetwork.wallet.ui.balance.BalanceOverview
import eu.peernetwork.wallet.ui.transfer.Transfer
import java.math.RoundingMode

@Composable
fun TransferCheckout(
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    onUserClicked: (String) -> Unit,
    onBack: () -> Unit,
    onFinish: () -> Unit
) {
    val context = LocalContext.current
    val component = remember {
        provider.builder(Transfer.Builder::class.java).build(context)
    }
    val viewModel = viewModel(
        modelClass = TransferViewModel::class.java,
        viewModelStoreOwner = viewModelStoreOwner,
        factory = component.viewModelFactory()
    )
    val state by viewModel.state.collectAsStateWithLifecycle()
    val status by viewModel.status.collectAsStateWithLifecycle()
    val handleFinish by rememberUpdatedState(onFinish)
    val streamState = remember { derivedStateOf {
        if (status is TransferViewModel.Status.Empty) {
            DesignStreamState.Default
        } else {
            DesignStreamState.Success(
                (status as? TransferViewModel.Status.Confirmation?)?.detail
                    ?: (status as? TransferViewModel.Status.Checkout?)?.detail!!
            )
        }
    } }
    val initialized = remember { mutableStateOf(false) }
    val lastUpdated = rememberSaveable { mutableLongStateOf(System.currentTimeMillis()) }
    val isLoading = remember { derivedStateOf { state is TransferViewModel.State.Loading } }
    DesignStream(
        state = streamState,
        default = { LaunchedEffect(Unit) { handleFinish() } }
    ) {
        val price = remember { derivedStateOf {
            it.value.amount.setScale(2, RoundingMode.HALF_UP)
        } }
        TransferPreview(
            price = price.value.toString(),
            recipient = it.value.recipient,
            message = it.value.message,
            isLoading = isLoading,
            onRefresh = { lastUpdated.longValue = System.currentTimeMillis() },
            onBack = onBack,
            onSend = {
                viewModel.transfer(
                    price = it.value.amount,
                    recipient = it.value.recipient.id,
                    message = it.value.message,
                )
            }
        ) {
            BalanceOverview(
                color = MaterialTheme.colorScheme.surfaceContainerLowest,
                provider = component,
                viewModelStoreOwner = viewModelStoreOwner
            ) { component, model ->
                LaunchedEffect(lastUpdated.longValue) {
                    if (initialized.value) {
                        model()
                    } else {
                        initialized.value = true
                    }
                }
                LaunchedEffect(state) {
                    if (state is TransferViewModel.State.Success) {
                        model()
                    }
                }
            }
        }
    }
    LaunchedEffect(Unit) {
        (status as? TransferViewModel.Status.Confirmation?)?.let {
            viewModel.checkout(it.detail)
        }
    }
    LaunchedEffect(state) {
        if (state is TransferViewModel.State.Success) {
            viewModel.reset()
        }
    }
}
