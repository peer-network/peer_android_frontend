package eu.peernetwork.wallet.ui.transfer

import android.content.res.Configuration
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.design.component.DesignStatefulScaffoldState
import eu.peernetwork.core.ui.design.compose.DesignAsyncImage
import eu.peernetwork.core.ui.design.compose.DesignAvatar
import eu.peernetwork.core.ui.design.compose.DesignButton
import eu.peernetwork.core.ui.design.compose.DesignDetailLayout
import eu.peernetwork.core.ui.theme.PeerAppGreen
import eu.peernetwork.core.ui.theme.PeerTheme
import eu.peernetwork.wallet.ui.R
import eu.peernetwork.wallet.ui.model.UiRecipient
import eu.peernetwork.wallet.ui.model.UiTransfer
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.UUID

@Composable
fun TransferDialog(
    state: State<DesignStatefulScaffoldState>,
    transfer: MutableState<UiTransfer?>,
    recipient: UiRecipient,
    onFinish: () -> Unit = {},
    onSubmit: (UiTransfer) -> Unit = {}
) {
    val isLoading = remember { derivedStateOf { state.value is DesignStatefulScaffoldState.Loading } }
    val isSuccessful = remember { derivedStateOf { state.value is DesignStatefulScaffoldState.Success<*> } }
    val hasError = remember { derivedStateOf { state.value is DesignStatefulScaffoldState.Error } }
    val handleOnSubmit by rememberUpdatedState(onSubmit)
    val handleOnFinish by rememberUpdatedState(onFinish)
    transfer.value?.let {
        AlertDialog(
            title = {
                Text(
                    text = if (isSuccessful.value) {
                        stringResource(id = R.string.transaction_label)
                    } else {
                        stringResource(id = R.string.checkout_label)
                    },
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.SemiBold
                    )
                )
            },
            text = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    DesignDetailLayout(
                        lead = {
                            DesignAvatar {
                                DesignAsyncImage(
                                    label = recipient.username,
                                    imageUrl = recipient.imageUrl,
                                    size = 48.dp,
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = MaterialTheme.colorScheme.tertiary,
                                    ),
                                    color = MaterialTheme.colorScheme.background,
                                    modifier = Modifier.clip(CircleShape)
                                        .wrapContentSize()
                                        .clipToBounds()
                                )
                            } },
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            "${it.token.setScale(4, RoundingMode.HALF_UP)}",
                            modifier = Modifier.padding(start = 12.dp),
                            style = MaterialTheme.typography.headlineLarge.copy(
                                color = MaterialTheme.colorScheme.onBackground,
                            ),
                        )
                        Text(
                            if (isSuccessful.value) {
                                stringResource(id = R.string.transfer_confirmation, recipient.username)
                            } else {
                                stringResource(R.string.transfer_caption, recipient.username)
                            },
                            modifier = Modifier.padding(start = 12.dp),
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = MaterialTheme.colorScheme.tertiary,
                            ),
                        )
                    }
                    Crossfade(isSuccessful.value) { target ->
                        Icon(
                            painter = if (target) {
                                painterResource(R.drawable.ic_check)
                            } else {
                                painterResource(R.drawable.ic_transfer)
                            },
                            contentDescription = null,
                            modifier = Modifier.size(18.dp),
                            tint = if (target) {
                                PeerAppGreen
                            } else {
                                MaterialTheme.colorScheme.surfaceDim
                            }
                        )
                    }
                }
            },
            confirmButton = {
                DesignButton(
                    isLoading = isLoading.value,
                    enabled = !isLoading.value,
                    onClick = {
                        if (isSuccessful.value) {
                            transfer.value = null
                            handleOnFinish()
                        } else {
                            handleOnSubmit(it)
                        }
                    },
                    minHeight = 36.dp,
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = PaddingValues(horizontal = 36.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = if (isSuccessful.value) {
                            stringResource(id = R.string.done_label)
                        } else {
                            stringResource(id = R.string.send_label)
                        },
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            },
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            onDismissRequest = {
                if (isSuccessful.value) {
                    handleOnFinish()
                }
                transfer.value = null },
        )
        LaunchedEffect(hasError.value) {
            if (hasError.value) {
                transfer.value = null
            }
        }
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun PreviewTransferDialog() {
    PeerTheme {
        val state = remember { mutableStateOf<DesignStatefulScaffoldState>(DesignStatefulScaffoldState.Empty) }
        val transfer = remember { mutableStateOf<UiTransfer?>(UiTransfer("johnDoe", BigDecimal(1.10))) }
        val recipient = UiRecipient(
            id = UUID.randomUUID().toString(),
            slug = "1234",
            username = "johnDoe",
            imageUrl = "http://localhost"
        )
        TransferDialog(state, transfer, recipient) {  }
    }
}
