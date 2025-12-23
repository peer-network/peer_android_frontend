package eu.peernetwork.wallet.ui.transfer

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.ViewModelStoreOwner
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.luna.DesignImage
import eu.peernetwork.core.ui.design.luna.DesignAvatar
import eu.peernetwork.core.ui.design.material.DesignDetailLayout
import eu.peernetwork.core.ui.extension.builder
import eu.peernetwork.core.ui.theme.PeerTheme
import eu.peernetwork.wallet.ui.model.UiRecipient
import eu.peernetwork.wallet.ui.model.UiTransfer
import java.util.UUID

@Composable
fun TransferScreen(
    tax: Double,
    recipient: UiRecipient,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    onClear: () -> Unit,
    onTransfer: (UiTransfer) -> Unit,
    onRecipientClick: (UiRecipient) -> Unit,
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
    val amount by rememberSaveable(stateSaver = TextFieldState.Saver) { mutableStateOf(TextFieldState()) }
    val handleOnTransfer by rememberUpdatedState(onTransfer)
    TransferScreen(
        recipient = recipient,
        onClear = onClear,
        onClick = onRecipientClick
    ) {
        TransferForm(amount, tax) {
            viewModel.reset()
            handleOnTransfer(UiTransfer(recipient.id, it))
        }
    }
}

@Composable
fun TransferScreen(
    recipient: UiRecipient,
    modifier: Modifier = Modifier,
    onClear: () -> Unit = {},
    onClick: (UiRecipient) -> Unit = {},
    content: @Composable () -> Unit
) {
    val handleOnClick by rememberUpdatedState(onClick)
    val updatedContent by rememberUpdatedState(content)
    Column(modifier) {
        DesignDetailLayout(
            lead = {
                DesignAvatar {
                    DesignImage(
                        label = recipient.username,
                        imageUrl = recipient.imageUrl,
                        size = 36.dp,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.outline,
                        ),
                        color = MaterialTheme.colorScheme.background,
                        modifier = Modifier.clip(CircleShape)
                            .wrapContentSize()
                            .clipToBounds()
                            .clickable(role = Role.Button) {
                                handleOnClick(recipient)
                            }
                    )
                }
            },
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 12.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    recipient.username,
                    modifier = Modifier.padding(start = 12.dp),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onBackground,
                    ),
                )
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    painter = painterResource(eu.peernetwork.core.ui.R.drawable.ic_plus),
                    contentDescription = null,
                    modifier = Modifier.size(12.dp).graphicsLayer {
                        rotationZ = 45f
                    }.clickable(role = Role.Button, onClick = onClear),
                    tint = MaterialTheme.colorScheme.surfaceDim
                )
            }
        }
        updatedContent()
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun PreviewTransferScreen() {
    PeerTheme {
        val state = remember { TextFieldState() }
        val recipient = UiRecipient(
            id = UUID.randomUUID().toString(),
            slug = "1234",
            username = "johnDoe",
            imageUrl = "http://localhost"
        )
        TransferScreen(recipient, Modifier.padding(16.dp)) {
            TransferForm(state, 4.0) {}
        }
    }
}
