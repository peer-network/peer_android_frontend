package eu.peernetwork.wallet.ui.transfer

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.design.luna.DesignButton
import eu.peernetwork.core.ui.design.luna.DesignRefreshScaffold
import eu.peernetwork.core.ui.design.luna.designSecondaryButtonColors
import eu.peernetwork.core.ui.design.material.DesignScaffold
import eu.peernetwork.core.ui.extension.value
import eu.peernetwork.core.ui.theme.DesignTheme
import eu.peernetwork.feature.wallet.ui.R
import eu.peernetwork.wallet.ui.model.UiRecipient
import java.math.BigDecimal

@Composable
fun TransferPage(
    balance: State<BigDecimal>,
    disable: MutableState<Boolean>,
    isLoading: MutableState<Boolean>,
    recipient: MutableState<UiRecipient?>,
    focusRequester: FocusRequester,
    onSearch: () -> Unit,
    onRefresh: () -> Unit,
    onUserClicked: (String) -> Unit,
    onSubmit: (UiRecipient, BigDecimal, String) -> Unit,
    content: @Composable () -> Unit,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val animatable = remember { Animatable(0f) }
    val handleSubmit by rememberUpdatedState(onSubmit)
    val updatedContent by rememberUpdatedState(content)
    val isRefreshing = remember { mutableStateOf(false) }
    val amount by rememberSaveable(stateSaver = TextFieldState.Saver) { mutableStateOf(TextFieldState()) }
    val isEnabled = remember { derivedStateOf {
        val currentAmount = amount.value.toBigDecimalOrNull()
        recipient.value != null
                && currentAmount != null
                && currentAmount > BigDecimal.ZERO
                && currentAmount <= balance.value
    } }
    val message by rememberSaveable(stateSaver = TextFieldState.Saver) { mutableStateOf(TextFieldState()) }
    DesignRefreshScaffold(isRefreshing, onRefresh = onRefresh) {
        DesignScaffold(
            alwaysReturn = true,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 18.dp),
            header = {
                Box(
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .padding(bottom = 10.dp)
                ) { updatedContent() }
            }
        ) {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState())
                .imePadding()
            ) {
                Crossfade(recipient.value) { targetState ->
                    targetState?.let {
                        TransferRecipient(
                            recipient = it,
                            onEdit = onSearch,
                            onClick = onUserClicked
                        )
                    } ?: TransferRecipient(onClick = onSearch)
                }
                TransferAmount(
                    state = amount,
                    isLoading = isLoading,
                    modifier = Modifier.padding(top = 10.dp),
                    focusRequester = focusRequester,
                )
                TransferMessage(
                    state = message,
                    modifier = Modifier.padding(top = 10.dp),
                )
                DesignButton(
                    enabled = isEnabled.value,
                    onClick = {
                        recipient.value?.let {
                            handleSubmit(
                                it,
                                amount.value.toBigDecimal(),
                                message.value
                            )
                        }
                    },
                    minHeight = 48.dp,
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    colors = designSecondaryButtonColors(),
                    modifier = Modifier
                        .padding(vertical = 16.dp)
                        .fillMaxWidth()
                ) { Text(stringResource(R.string.continue_label)) }
            }
        }
    }
    LaunchedEffect(Unit) {
        snapshotFlow { disable.value }
            .collect { isDisabled ->
                if (!isDisabled) {
                    recipient.value?.let {
                        focusRequester.requestFocus()
                        keyboardController?.show()
                    }
                }
            }
    }
    LaunchedEffect(Unit) {
        animatable.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 1)
        )
        recipient.value?.let {
            focusRequester.requestFocus()
        }
    }
}

@Preview
@Composable
fun PreviewTransferPage() {
    val disable = remember { mutableStateOf(false) }
    val balance = remember { mutableStateOf(BigDecimal.ZERO) }
    val isLoading = remember { mutableStateOf(false) }
    val recipient = remember { mutableStateOf<UiRecipient?>(null) }
    val focusRequester = FocusRequester()
    DesignTheme(isDarkMode = true) {
        TransferPage(
            balance = balance,
            disable = disable,
            isLoading = isLoading,
            recipient = recipient,
            focusRequester = focusRequester,
            onSearch = {},
            onRefresh = {},
            onUserClicked = {},
            onSubmit = { _,_,_ -> }
        ) {}
    }
}
