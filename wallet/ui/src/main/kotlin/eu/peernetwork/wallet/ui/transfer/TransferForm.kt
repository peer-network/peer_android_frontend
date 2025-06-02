package eu.peernetwork.wallet.ui.transfer

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.design.compose.DesignAsyncImage
import eu.peernetwork.core.ui.design.compose.DesignAvatar
import eu.peernetwork.core.ui.design.compose.DesignDetailLayout
import eu.peernetwork.core.ui.design.compose.DesignLabel
import eu.peernetwork.core.ui.design.compose.DesignTextField
import eu.peernetwork.core.ui.extension.toFloat
import eu.peernetwork.core.ui.theme.PeerTheme
import eu.peernetwork.wallet.ui.R
import eu.peernetwork.wallet.ui.model.UiRecipient
import java.math.BigDecimal
import java.util.UUID

@Composable
fun TransferForm(
    state: TextFieldState,
    recipient: UiRecipient,
    error: String? = null,
    isLoading: Boolean = false,
    onClick: (UiRecipient) -> Unit = {},
    onClear: () -> Unit = {},
    onSubmit: (BigDecimal) -> Unit,
) {
    val isValidated = remember { derivedStateOf { state.text.toString().toBigDecimalOrNull() != null } }
    val handleOnClick by rememberUpdatedState(onClick)
    val handleSubmission by rememberUpdatedState {
        state.text.toString().toBigDecimalOrNull()?.let {
            onSubmit(it)
        }
    }
    Column {
        DesignDetailLayout(
            lead = {
                Box(modifier = Modifier
                    .clip(CircleShape)
                    .wrapContentSize()
                    .clipToBounds()
                ) {
                    DesignAvatar {
                        DesignAsyncImage(
                            label = recipient.username,
                            imageUrl = recipient.imageUrl,
                            size = 36.dp,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = MaterialTheme.colorScheme.tertiary,
                            ),
                            color = MaterialTheme.colorScheme.background,
                            modifier = Modifier.clickable(role = Role.Button) {
                                handleOnClick(recipient)
                            }
                        )
                    }
                } },
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 8.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    recipient.username,
                    modifier = Modifier.padding(start = 12.dp),
                    style = MaterialTheme.typography.bodySmall.copy(
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
        Row {
            Column(modifier = Modifier.padding(start = 48.dp)) {
                DesignLabel(
                    visible = error != null,
                    label = {
                        error?.run {
                            Text(
                                text = this,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = MaterialTheme.colorScheme.error
                                ),
                                modifier = Modifier
                                    .padding(horizontal = 16.dp)
                                    .padding(top = 8.dp)
                            )
                        }
                    }
                ) {
                    DesignTextField(
                        state = state,
                        enabled = !isLoading,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Decimal,
                            imeAction = ImeAction.Done
                        ),
                        trailing = {
                            Icon(
                                painter = painterResource(eu.peernetwork.core.ui.R.drawable.ic_share),
                                contentDescription = stringResource(R.string.transfer_label),
                                modifier = Modifier.size(12.dp)
                                    .clickable(role = Role.Button) {
                                        handleSubmission() }
                                    .graphicsLayer { alpha = isValidated.value.toFloat() },
                                tint = MaterialTheme.colorScheme.surfaceDim
                            )
                        },
                        colors = TextFieldDefaults.colors(
                            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                            focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                            focusedTextColor = MaterialTheme.colorScheme.onBackground,
                            unfocusedTextColor = MaterialTheme.colorScheme.tertiary,
                            focusedPlaceholderColor = MaterialTheme.colorScheme.surfaceDim,
                            unfocusedPlaceholderColor = MaterialTheme.colorScheme.surfaceTint,
                        ),
                    ) { Text(stringResource(R.string.amount_label)) }
                }
                Text(
                    stringResource(R.string.transfer_disclaimer, "4%"),
                    modifier = Modifier.padding(top = 12.dp)
                        .padding(horizontal = 16.dp),
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = MaterialTheme.colorScheme.tertiary,
                    ),
                )
            }
        }
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun PreviewTransferForm() {
    PeerTheme {
        val state = remember { TextFieldState() }
        val recipient = UiRecipient(
            id = UUID.randomUUID().toString(),
            slug = "123456",
            username = "johnDoe",
            imageUrl = "http://localhost"
        )
        Column(Modifier.padding(16.dp)) {
            TransferForm(state, recipient) {}
            Spacer(modifier = Modifier.height(24.dp))
            TransferForm(state, recipient, "Hello, world!") {}
        }
    }
}
