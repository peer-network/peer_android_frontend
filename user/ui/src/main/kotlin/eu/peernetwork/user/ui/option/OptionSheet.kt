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
import eu.peernetwork.user.ui.R

@Composable
fun OptionSheet(
    state: MutableState<Boolean>,
    onMenuClicked: () -> Unit
) {
    val confirmed = remember { mutableStateOf(false) }
    val handleConfirm by rememberUpdatedState(onMenuClicked)
    DesignBottomSheetScaffold(
        state = state,
        color = MaterialTheme.colorScheme.surfaceDim,
        onDismiss = {
            state.value = false
            if (confirmed.value) {
                handleConfirm()
                confirmed.value = false
            }
        }
    ) {
        OptionSheet(
            onMenuClicked = {
                confirmed.value = true
                state.value = false
            }
        ) { state.value = false }
    }
}

@Composable
fun OptionSheet(
    onMenuClicked: () -> Unit,
    onCancel: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 24.dp)
            .navigationBarsPadding()
    ) {
        DesignItem(
            label = stringResource(R.string.ads_label),
            painter = painterResource(R.drawable.ic_ads),
            onClick = onMenuClicked
        )
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
        OptionSheet({}) {}
    }
}
