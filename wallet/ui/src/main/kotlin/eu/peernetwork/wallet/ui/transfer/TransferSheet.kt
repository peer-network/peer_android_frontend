package eu.peernetwork.wallet.ui.transfer

import android.content.res.Configuration
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.compose.DesignStatefulScaffoldState
import eu.peernetwork.core.ui.design.material.DesignBottomSheetScaffold
import eu.peernetwork.core.ui.extension.builder
import eu.peernetwork.core.ui.theme.PeerTheme
import eu.peernetwork.wallet.ui.R
import eu.peernetwork.wallet.ui.model.UiRecipient
import eu.peernetwork.wallet.ui.model.UiTransfer
import java.math.BigDecimal
import java.util.UUID
import eu.peernetwork.wallet.ui.compose.TickButton
import eu.peernetwork.wallet.ui.compose.ToastLayout

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun TransferSheet(
    transfer: MutableState<UiTransfer?>,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    recipient: UiRecipient,
    onFinish: () -> Unit = {},
    onRecipientClick: (UiRecipient) -> Unit
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
    val derivedState = remember {
        derivedStateOf {
            when (state) {
                TransferViewModel.State.Empty -> DesignStatefulScaffoldState.Empty
                TransferViewModel.State.Loading -> DesignStatefulScaffoldState.Loading
                is TransferViewModel.State.Success -> {
                    DesignStatefulScaffoldState.Success(
                        (state as TransferViewModel.State.Success).transfer
                    )
                }
                is TransferViewModel.State.Error -> DesignStatefulScaffoldState.Error(
                    (state as TransferViewModel.State.Error).error
                )
            }
        }
    }
    val isLoading = remember { derivedStateOf { state is TransferViewModel.State.Loading } }
    val isSuccessful = remember { derivedStateOf { derivedState.value is DesignStatefulScaffoldState.Success<*> } }
    val error = remember { derivedStateOf {
        (derivedState.value as? DesignStatefulScaffoldState.Error?)?.error?.message?.let {
            component.resource().string(it)
        }
    }}
    val showRecipient = remember { mutableStateOf(false) }
    val showSheet = remember(transfer.value) { mutableStateOf(transfer.value != null) }
    val handleOnFinish by rememberUpdatedState(onFinish)
    val handleOnRecipientClick by rememberUpdatedState(onRecipientClick)
    DesignBottomSheetScaffold(
        state = showSheet,
        onDismiss = {
            if (isSuccessful.value) {
                viewModel.reset()
                handleOnFinish()
            } else if (showRecipient.value) {
                showRecipient.value = false
                handleOnRecipientClick(recipient)
            }
            transfer.value = null
        },
    ) {
        Crossfade(transfer.value) { target ->
            if (target != null) {
                TransferSheet(
                    isLoading,
                    error,
                    isSuccessful,
                    recipient,
                    target.token,
                    {
                        showSheet.value = false
                        showRecipient.value = true }
                ) {
                    if (isSuccessful.value) {
                        showSheet.value = false
                    } else {
                        viewModel.transfer(target.recipient, target.token)
                    }
                }
            }
        }
    }
}

@Composable
fun TransferSheet(
    state: State<Boolean>,
    error: State<String?>,
    isSuccessful: State<Boolean>,
    recipient: UiRecipient,
    token: BigDecimal,
    onClick: () -> Unit = {},
    onSubmit: () -> Unit
) {
    Crossfade(isSuccessful.value) { target ->
        if (target) {
            Box {
                TransferSheetScaffold(
                    state = state,
                    error = error,
                    title = stringResource(R.string.sent_label),
                    recipient = recipient,
                    token = token,
                    action = stringResource(R.string.close_label),
                    onClick = onClick,
                    onSubmit = onSubmit,
                ) {
                    TickButton(
                        modifier = Modifier.size(36.dp),
                        rawRes = R.raw.tick
                    )
                }
                ToastLayout()
            }
        } else {
            TransferSheetScaffold(
                state = state,
                error = error,
                title = stringResource(R.string.recipient_label),
                recipient = recipient,
                token = token,
                action = stringResource(R.string.send_label),
                onClick = onClick,
                onSubmit = onSubmit,
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_transfer),
                    contentDescription = null,
                    modifier = Modifier.size(18.dp),
                    tint = MaterialTheme.colorScheme.surfaceDim
                )
            }
        }
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun PreviewTransferSheet() {
    PeerTheme {
        Column {
            val recipient = UiRecipient(
                id = UUID.randomUUID().toString(),
                slug = "1234",
                username = "johnDoe",
                imageUrl = "http://localhost"
            )
            TransferSheet(
                state = remember { mutableStateOf(true) },
                error = remember { mutableStateOf(null) },
                isSuccessful = remember { mutableStateOf(false) },
                recipient = recipient,
                token = BigDecimal(1.0),
                {}
            ) {}
            Spacer(modifier = Modifier.height(16.dp))
            TransferSheet(
                state = remember { mutableStateOf(false) },
                error = remember { mutableStateOf("Error message...") },
                isSuccessful = remember { mutableStateOf(false) },
                recipient = recipient,
                token = BigDecimal(1.0),
                {}
            ) {}
            Spacer(modifier = Modifier.height(16.dp))
            TransferSheet(
                state = remember { mutableStateOf(false) },
                error = remember { mutableStateOf(null) },
                isSuccessful = remember { mutableStateOf(true) },
                recipient = recipient,
                token = BigDecimal(1.0),
                {}
            ) {}
        }
    }
}
