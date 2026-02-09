package eu.peernetwork.user.ui.option

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.design.luna.DesignItem
import eu.peernetwork.core.ui.design.material.DesignBottomSheetScaffold
import eu.peernetwork.core.ui.theme.DesignTheme
import eu.peernetwork.feature.user.ui.R

@Composable
fun OptionSheet(
    isAdmin: Boolean,
    state: MutableState<Boolean>,
    onBlock: () -> Unit,
    onMenuClicked: () -> Unit
) {
    val confirmed = remember { mutableStateOf(false) }
    val handleBlock by rememberUpdatedState(onBlock)
    val handleConfirm by rememberUpdatedState(onMenuClicked)
    val isBlock = remember { mutableStateOf(false) }
    DesignBottomSheetScaffold(
        state = state,
        color = MaterialTheme.colorScheme.surfaceDim,
        dismissable = true,
        onDismiss = {
            state.value = false
            if (confirmed.value) {
                handleConfirm()
                confirmed.value = false
            } else if (isBlock.value) {
                handleBlock()
                isBlock.value = false
            }
        }
    ) {
        OptionSheet(
            isAdmin = isAdmin,
            onBlock = {
                isBlock.value = true
                state.value = false
            },
            onMenuClicked = {
                confirmed.value = true
                state.value = false
            }
        ) { state.value = false }
    }
}

@Composable
fun OptionSheet(
    isAdmin: Boolean,
    onBlock: () -> Unit,
    onMenuClicked: () -> Unit,
    onCancel: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 24.dp)
            .navigationBarsPadding()
    ) {
        if (isAdmin) {
            DesignItem(
                label = stringResource(R.string.ads_label),
                painter = painterResource(R.drawable.ic_ads),
                onClick = onMenuClicked
            )
        } else {
            DesignItem(
                label = stringResource(R.string.report_label),
                size = 20.dp,
                minSize = 28.dp,
                painter = painterResource(R.drawable.ic_report),
                onClick = onBlock
            )
        }
        DesignItem(
            label = stringResource(R.string.cancel_text),
            painter = painterResource(R.drawable.ic_cancel),
            color = MaterialTheme.colorScheme.error,
            tint = MaterialTheme.colorScheme.error,
            onClick = onCancel
        )
    }
}

@Composable
@Preview
@OptIn(ExperimentalMaterial3Api::class)
fun PreviewOptionSheet() {
    DesignTheme(isDarkMode = true) {
        OptionSheet(true, {}, {}) {}
    }
}
