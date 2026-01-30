package eu.peernetwork.wallet.ui.transfer.v2

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.design.luna.DesignRefreshScaffold
import eu.peernetwork.core.ui.design.material.DesignScaffold
import eu.peernetwork.wallet.ui.model.UiRecipient

@Composable
fun TransferPage(
    disable: MutableState<Boolean>,
    isLoading: MutableState<Boolean>,
    recipient: MutableState<UiRecipient?>,
    focusRequester: FocusRequester,
    onSearch: () -> Unit,
    onRefresh: () -> Unit,
    onUserClicked: (String) -> Unit,
    content: @Composable () -> Unit,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val animatable = remember { Animatable(0f) }
    val updatedContent by rememberUpdatedState(content)
    val isRefreshing = remember { mutableStateOf(false) }
    val amount by rememberSaveable(stateSaver = TextFieldState.Saver) { mutableStateOf(TextFieldState()) }
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
            Column {
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
