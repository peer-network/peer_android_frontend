package eu.peernetwork.wallet.ui.service

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModelStoreOwner
import eu.peernetwork.wallet.ui.R
import eu.peernetwork.wallet.ui.compose.ExpandableLabel
import eu.peernetwork.wallet.ui.model.UiTransfer
import eu.peernetwork.wallet.ui.transfer.TransferScreen
import eu.peernetwork.wallet.ui.transfer.TransferSheet

@Composable
fun ServiceTransfer(
    tax: Double,
    state: MutableState<ServiceState>,
    component: Service.Component,
    viewModelStoreOwner: ViewModelStoreOwner,
    onAccountClicked: (String) -> Unit,
    onClear: () -> Unit,
    onClick: () -> Unit,
) {
    val lastState = remember(state.value) { mutableStateOf(state.value) }
    var showTransfer = rememberSaveable { mutableStateOf(false) }
    var transaction = remember { mutableStateOf<UiTransfer?>(null) }
    val handleOnClear by rememberUpdatedState(onClear)
    val handleOnAccountClicked by rememberUpdatedState(onAccountClicked)
    ExpandableLabel(
        stringResource(R.string.transfer_label),
        showTransfer,
        {
            if (lastState.value is ServiceState.Default && !it) {
                handleOnClear()
            }
            state.value = lastState.value
        }
    ) {
        ServiceScreen(
            stringResource(R.string.recipient_selection_label),
            state,
            onClick,
        ) {
            TransferScreen(
                tax = tax,
                recipient = it.recipient,
                provider = component,
                viewModelStoreOwner = viewModelStoreOwner,
                onClear = {
                    lastState.value = ServiceState.Default
                    showTransfer.value = false },
                onTransfer = { transaction.value = it }
            ) { handleOnAccountClicked(it.id) }
        }
    }
    (state.value as? ServiceState.Transfer?)?.let {
        TransferSheet(
            transaction,
            component,
            viewModelStoreOwner,
            it.recipient,
            {
                transaction.value = null
                lastState.value = ServiceState.Default
                showTransfer.value = false
            }
        ) { handleOnAccountClicked(it.id) }
    }
}

