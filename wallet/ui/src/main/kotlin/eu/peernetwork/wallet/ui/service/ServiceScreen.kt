package eu.peernetwork.wallet.ui.service

import android.content.res.Configuration
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.compose.DesignOutlinedButton
import eu.peernetwork.core.ui.theme.PeerTheme
import eu.peernetwork.wallet.ui.R
import eu.peernetwork.wallet.ui.compose.ExpandableLabel
import eu.peernetwork.wallet.ui.model.UiRecipient
import java.util.UUID

sealed interface ServiceState {
    data object Default : ServiceState
    data class Transfer(val recipient: UiRecipient) : ServiceState
}

@Composable
fun ServiceScreen(
    state: MutableState<ServiceState>,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    onAccountClicked: (String) -> Unit,
    onClick: () -> Unit,
) {
    val lastState = remember(state.value) { mutableStateOf(state.value) }
    var showTransfer = rememberSaveable { mutableStateOf(false) }
    ExpandableLabel(
        stringResource(R.string.transfer_label),
        showTransfer,
        { state.value = lastState.value }
    ) {
        ServiceScreen(
            stringResource(R.string.recipient_selection_label),
            state,
            {  },
        ) {

        }
    }
}

@Composable
fun ServiceScreen(
    name: String,
    state: State<ServiceState>,
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
    val updateContent by rememberUpdatedState(content)
    Crossfade(state.value) { target ->
        when(target) {
            ServiceState.Default -> {
                DesignOutlinedButton(
                    onClick = onClick,
                    shape = RoundedCornerShape(8.dp),
                    textStyle = MaterialTheme.typography.bodySmall,
                    contentPadding = PaddingValues(vertical = 8.dp, horizontal = 32.dp),
                    modifier = Modifier
                        .padding(start = 4.dp)
                        .height(36.dp)
                        .fillMaxWidth(),
                    content = {
                        Text(text = name, style = MaterialTheme.typography.bodySmall)
                    }
                )
            }
            is ServiceState.Transfer -> { updateContent() }
        }
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun PreviewServiceScreen() {
    PeerTheme {
        Column {
            val recipient = UiRecipient(
                id = UUID.randomUUID().toString(),
                slug = "1234",
                username = "johnDoe",
                imageUrl = "http://localhost"
            )
            ServiceScreen(
                "ServiceScreen",
                remember { mutableStateOf(ServiceState.Transfer(recipient)) },
                {}
            ) {
                Text(text = "Content", style = MaterialTheme.typography.bodySmall)
            }
            Spacer(modifier = Modifier.height(16.dp))
            ServiceScreen(
                "ServiceScreen",
                remember { mutableStateOf(ServiceState.Default) },
                {}
            ) {}
        }
    }
}
