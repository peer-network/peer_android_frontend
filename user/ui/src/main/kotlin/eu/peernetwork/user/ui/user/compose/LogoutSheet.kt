package eu.peernetwork.user.ui.user.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.compose.DesignBottomSheet
import eu.peernetwork.core.ui.compose.DesignButton
import eu.peernetwork.core.ui.compose.DesignOverlay
import eu.peernetwork.core.ui.compose.DesignOverlayBackground
import eu.peernetwork.core.ui.theme.PeerTheme
import eu.peernetwork.user.ui.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogoutSheet(
    state: MutableState<Boolean>,
    initialValue: SheetValue = SheetValue.Hidden,
    onLogout: () -> Unit = {}
) {
    DesignBottomSheet(
        showSheet = state,
        tag = "logoutSheet",
        onDismissRequest = { state.value = false },
        initialValue = initialValue,
        color = MaterialTheme.colorScheme.surfaceContainerLow,
        background = {
            DesignOverlayBackground(
                state = state,
                modifier = Modifier.fillMaxSize()
                    .background(MaterialTheme.colorScheme.background.copy(alpha = .6f))
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(24.dp)
        ) {
            Text(
                stringResource(R.string.logout_message),
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.tertiary
                ),
                modifier = Modifier.fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            DesignButton(
                enabled = state.value,
                onClick = onLogout,
                modifier = Modifier.fillMaxWidth()
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
}

@Composable
@Preview
@OptIn(ExperimentalMaterial3Api::class)
fun PreviewLogoutSheet() {
    PeerTheme {
        DesignOverlay {
            LogoutSheet(
                state = remember { mutableStateOf(true) },
                initialValue = SheetValue.Expanded
            )
        }
    }
}
