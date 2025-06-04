package eu.peernetwork.wallet.ui.transfer

import android.content.res.Configuration
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.clearText
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.component.DesignStatefulScaffoldState
import eu.peernetwork.core.ui.design.compose.DesignCard
import eu.peernetwork.core.ui.design.compose.DesignOutlinedButton
import eu.peernetwork.core.ui.extension.builder
import eu.peernetwork.core.ui.theme.PeerTheme
import eu.peernetwork.wallet.ui.R
import eu.peernetwork.wallet.ui.compose.ExpandableOption
import eu.peernetwork.wallet.ui.model.UiRecipient
import eu.peernetwork.wallet.ui.model.UiTransfer
import java.util.UUID

@Composable
fun TransferScreen(
    recipient: MutableState<UiRecipient?>,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    onRecipientClick: (UiRecipient) -> Unit,
    onClick: () -> Unit,
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
    val isLoading = remember { derivedStateOf { state is TransferViewModel.State.Loading } }
    val error = remember { derivedStateOf {
        (state as? TransferViewModel.State.Error?)?.error?.message?.let {
            component.resource().string(it)
        }
    } }
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
    var showLabel = rememberSaveable { mutableStateOf(false) }
    var transaction = remember { mutableStateOf<UiTransfer?>(null) }
    val lastRecipient = remember(recipient.value) { mutableStateOf(recipient.value) }
    val amount by rememberSaveable(stateSaver = TextFieldState.Saver) { mutableStateOf(TextFieldState()) }
    val handleOnClick by rememberUpdatedState(onClick)
    TransferScreen(showLabel, { recipient.value = lastRecipient.value }) {
        Crossfade(recipient.value) { target ->
            if (target == null) {
                DesignOutlinedButton(
                    onClick = {
                        viewModel.reset()
                        amount.clearText()
                        handleOnClick()
                    },
                    shape = RoundedCornerShape(8.dp),
                    textStyle = MaterialTheme.typography.bodySmall,
                    contentPadding = PaddingValues(vertical = 8.dp, horizontal = 32.dp),
                    modifier = Modifier
                        .padding(start = 48.dp)
                        .height(36.dp)
                        .fillMaxWidth(),
                    content = {
                        Text(
                            text = stringResource(R.string.recipient_selection_label),
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                )
            } else {
                TransferForm(
                    amount,
                    target,
                    error.value,
                    isLoading.value,
                    onRecipientClick,
                    {
                        lastRecipient.value = null
                        showLabel.value = false
                    }
                ) {
                    viewModel.reset()
                    transaction.value = UiTransfer(target.id, amount.text.toString().toBigDecimal())
                }
            }
        }
    }
    recipient.value?.let {
        TransferDialog(derivedState, transaction, it, {
            lastRecipient.value = null
            showLabel.value = false
            amount.clearText()
            viewModel.reset()
        }) { viewModel.transfer(it.recipient, it.token) }
    }
}

@Composable
fun TransferScreen(
    state: MutableState<Boolean>,
    onAnimationEnd: (Boolean) -> Unit = { },
    content: @Composable () -> Unit
) {
    val border = MaterialTheme.colorScheme.tertiaryContainer
    val updatedContent by rememberUpdatedState(content)
    DesignCard(
        color = MaterialTheme.colorScheme.surfaceVariant,
        contentPadding = PaddingValues(0.dp),
        shape = RoundedCornerShape(24.dp)
    ) {
        ExpandableOption(
            state = state,
            onAnimationEnd = onAnimationEnd,
            icon = {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(MaterialTheme.colorScheme.background)
                        .padding(12.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_transaction),
                        contentDescription = stringResource(R.string.transfer_label),
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            },
            items = {
                Box(
                    modifier = Modifier
                        .drawBehind {
                            drawLine(
                                color = border.copy(alpha = .6f),
                                start = Offset(0f, 0f),
                                end = Offset(size.width, 0f),
                                strokeWidth = 1.dp.toPx()
                            )
                        }
                        .padding(vertical = 16.dp)
                ) { updatedContent() }
            }
        ) { Text(
            stringResource(R.string.transfer_label),
            modifier = Modifier.padding(start = 12.dp)
        ) }
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun PreviewTransferScreen() {
    PeerTheme {
        val amount = remember { TextFieldState() }
        val recipient = UiRecipient(
            id = UUID.randomUUID().toString(),
            slug = "1234",
            username = "johnDoe",
            imageUrl = "http://localhost"
        )
        var showLabel = rememberSaveable { mutableStateOf(false) }
        Column {
            TransferScreen(showLabel) {
                Box(modifier = Modifier.padding(top = 8.dp, bottom = 16.dp)) {
                    DesignOutlinedButton(
                        onClick = {  },
                        shape = RoundedCornerShape(8.dp),
                        textStyle = MaterialTheme.typography.bodySmall,
                        contentPadding = PaddingValues(vertical = 8.dp, horizontal = 32.dp),
                        modifier = Modifier
                            .padding(start = 48.dp)
                            .height(36.dp)
                            .fillMaxWidth(),
                        content = {
                            Text(
                                text = stringResource(R.string.recipient_selection_label),
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    )
                }
            }
            Spacer(modifier = Modifier.size(8.dp))
            TransferScreen(showLabel) {
                Box(modifier = Modifier.padding(top = 8.dp, bottom = 16.dp)) {
                    TransferForm(amount, recipient) {}
                }
            }
        }
    }
}
