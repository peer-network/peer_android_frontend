package eu.peernetwork.user.ui.logout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.design.luna.DesignButton
import eu.peernetwork.core.ui.design.luna.DesignOutlineButton
import eu.peernetwork.core.ui.design.luna.designSecondaryButtonColors
import eu.peernetwork.core.ui.design.material.DesignBottomSheetScaffold
import eu.peernetwork.core.ui.theme.DesignTheme
import eu.peernetwork.feature.user.ui.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogoutSheet(
    state: MutableState<Boolean>,
    isLoading: State<Boolean>,
    onLogout: () -> Unit = {}
) {
    val handleLogout by rememberUpdatedState(onLogout)
    val action = remember { mutableStateOf<(() -> Unit)?>(null) }
    DesignBottomSheetScaffold(
        state = state,
        dismissable = true,
        color = MaterialTheme.colorScheme.surfaceDim,
        onDismiss = {
            action.value?.invoke()
            action.value = null
            state.value = false
        }
    ) {
        LogoutSheet(
            isLoading = isLoading,
            onCancel = { state.value = false },
            modifier = Modifier
        ) {
            action.value = handleLogout
            state.value = false
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun LogoutSheet(
    isLoading: State<Boolean>,
    modifier: Modifier = Modifier,
    onCancel: () -> Unit,
    onLogout: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(top = 16.dp)
            .padding(bottom = 12.dp)
            .navigationBarsPadding()
            .then(modifier)
    ) {
        LogoutTitle()
        Text(
            stringResource(R.string.logout_message),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.outline,
            modifier = Modifier.fillMaxWidth()
                .padding(top = 12.dp)
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.padding(top = 16.dp)
                .padding(bottom = 6.dp)
        ) {
            DesignOutlineButton(
                onClick = onCancel,
                minHeight = 42.dp,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                contentPadding = PaddingValues(horizontal = 12.dp),
                modifier = Modifier.weight(1f)
            ) { Text(stringResource(R.string.cancel_label)) }
            DesignButton(
                onClick = onLogout,
                minHeight = 42.dp,
                enabled = !isLoading.value,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                colors = designSecondaryButtonColors(),
                contentPadding = PaddingValues(horizontal = 12.dp),
                modifier = Modifier.weight(1f)
            ) { Text(stringResource(R.string.logout_text)) }
        }
    }
}

@Composable
@Preview
@OptIn(ExperimentalMaterial3Api::class)
fun PreviewLogoutSheet() {
    DesignTheme(isDarkMode = true) {
        val isLoading = remember { mutableStateOf(false) }
        LogoutSheet(
            isLoading = isLoading,
            onCancel = {}
        ) {}
    }
}
