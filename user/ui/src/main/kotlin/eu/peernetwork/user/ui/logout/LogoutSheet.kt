package eu.peernetwork.user.ui.logout

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.design.material.DesignBottomSheetScaffold
import eu.peernetwork.core.ui.design.material.DesignButton
import eu.peernetwork.core.ui.theme.PeerTheme
import eu.peernetwork.user.ui.R

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
        color = MaterialTheme.colorScheme.surfaceContainerLow,
        onDismiss = {
            action.value?.invoke()
            action.value = null
            state.value = false
        }
    ) {
        LogoutSheet(
            state = state,
            isLoading = isLoading,
            onLogout = {
                action.value = handleLogout
                state.value = false
            },
            modifier = Modifier
        )
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun LogoutSheet(
    state: MutableState<Boolean>,
    isLoading: State<Boolean>,
    modifier: Modifier,
    onLogout: () -> Unit = {},
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp)
            .then(modifier)
    ) {
        Text(
            stringResource(R.string.logout_message),
            style = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.tertiary
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        DesignButton(
            enabled = state.value && !isLoading.value,
            onClick = onLogout,
            modifier = Modifier.fillMaxWidth(),
            isLoading = isLoading.value
        ) {
            Text(
                text = stringResource(R.string.logout_text),
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Bold
                )
            )
        }
        Spacer(modifier = Modifier.navigationBarsPadding())
    }
}

@Composable
@Preview
@OptIn(ExperimentalMaterial3Api::class)
fun PreviewLogoutSheet() {
    PeerTheme {
        val isLoading = remember { mutableStateOf(false) }
        LogoutSheet(
            state = remember { mutableStateOf(true) },
            isLoading = isLoading,
        )
    }
}
