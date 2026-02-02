package eu.peernetwork.wallet.ui.transfer

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.extension.builder
import eu.peernetwork.wallet.ui.balance.BalanceOverview
import eu.peernetwork.wallet.ui.model.UiRecipient
import eu.peernetwork.wallet.ui.model.UiTransferDetail

sealed interface TransferState {
    data object Default : TransferState
    data class Transfer(val recipient: UiRecipient) : TransferState
}

@Composable
fun TransferScreen(
    disable: MutableState<Boolean>,
    recipient: MutableState<UiRecipient?>,
    focusRequester: FocusRequester,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    onUserClicked: (String) -> Unit,
    onClick: () -> Unit,
    onSubmit: () -> Unit
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
    val status by viewModel.status.collectAsStateWithLifecycle()
    val handleSubmit by rememberUpdatedState(onSubmit)
    val isLoading = remember { mutableStateOf(false) }
    val initialized = remember { mutableStateOf(false) }
    val lastUpdated = rememberSaveable { mutableLongStateOf(System.currentTimeMillis()) }
    TransferPage(
        disable = disable,
        isLoading = isLoading,
        recipient = recipient,
        focusRequester = focusRequester,
        onSearch = onClick,
        onRefresh = { lastUpdated.longValue = System.currentTimeMillis() },
        onUserClicked = onUserClicked,
        onSubmit = { recipient, amount, message ->
            viewModel.proceed(
                detail = UiTransferDetail(
                    amount = amount,
                    message = message,
                    recipient = recipient
                )
            )
        }
    ) {
        BalanceOverview(
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
        }
    }
    LaunchedEffect(status) {
        if (status is TransferViewModel.Status.Confirmation) {
            handleSubmit()
        }
    }
}
