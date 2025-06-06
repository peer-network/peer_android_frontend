package eu.peernetwork.wallet.ui.transfer

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.design.component.DesignStatefulScaffoldState
import eu.peernetwork.core.ui.design.compose.DesignAsyncImage
import eu.peernetwork.core.ui.design.compose.DesignAvatar
import eu.peernetwork.core.ui.design.compose.DesignBottomSheet
import eu.peernetwork.core.ui.design.compose.DesignButton
import eu.peernetwork.core.ui.design.compose.DesignCard
import eu.peernetwork.core.ui.design.compose.DesignDetailLayout
import eu.peernetwork.core.ui.design.compose.DesignOverlayBackground
import eu.peernetwork.core.ui.extension.annotate
import eu.peernetwork.core.ui.theme.PeerAppGreen
import eu.peernetwork.core.ui.theme.PeerTheme
import eu.peernetwork.wallet.ui.R
import eu.peernetwork.wallet.ui.model.UiRecipient
import eu.peernetwork.wallet.ui.model.UiTransfer
import java.math.BigDecimal
import java.math.RoundingMode

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun TransferSheet(
    state: State<DesignStatefulScaffoldState>,
    showSheet: MutableState<Boolean>,
    transfer: MutableState<UiTransfer?>,
    recipient: UiRecipient,
    onFinish: () -> Unit = {},
    onRecipientClick: (UiRecipient) -> Unit,
    onSubmit: (UiTransfer) -> Unit = {}
) {
    val isLoading = remember { derivedStateOf { state.value is DesignStatefulScaffoldState.Loading } }
    val isSuccessful = remember { derivedStateOf { state.value is DesignStatefulScaffoldState.Success<*> } }
    val hasError = remember { derivedStateOf { state.value is DesignStatefulScaffoldState.Error } }
    val handleOnFinish by rememberUpdatedState(onFinish)
    val handleOnRecipientClick by rememberUpdatedState(onRecipientClick)
    val handleOnSubmit by rememberUpdatedState(onSubmit)
    DesignBottomSheet(
        tag = "TransferSheet",
        showSheet = showSheet,
        onDismissRequest = {
            if (isSuccessful.value) {
                handleOnFinish()
            }
            showSheet.value = false
        },
        background = {
            DesignOverlayBackground(
                state = it,
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background.copy(alpha = .6f))
            )
        }
    ) {
        val model = remember(it.value) { mutableStateOf<UiTransfer?>(transfer.value) }
        model.value?.let { transaction ->
            TransferSheet(
                isLoading,
                isSuccessful,
                recipient.username,
                recipient.slug,
                recipient.imageUrl,
                transaction.token,
                { handleOnRecipientClick(recipient) }
            ) {
                if (isSuccessful.value) {
                    handleOnFinish()
                } else {
                    handleOnSubmit(transaction)
                }
            }
        }
    }
    LaunchedEffect(hasError.value) {
        if (hasError.value) {
            showSheet.value = false
        }
    }
}

@Composable
fun TransferSheet(
    state: State<Boolean>,
    isSuccessful: State<Boolean>,
    username: String,
    slug: String,
    imageUrl: String,
    token: BigDecimal,
    onClick: () -> Unit = {},
    onSubmit: () -> Unit
) {
    val slugTag = "#$slug"
    Column(
        modifier = Modifier.padding(16.dp)
            .navigationBarsPadding()
    ) {
        Text(
            stringResource(R.string.amount_text),
            style = MaterialTheme.typography.bodySmall.copy(
                color = MaterialTheme.colorScheme.tertiary
            ),
            modifier = Modifier.padding(horizontal = 12.dp)
        )
        Text(
            "${token.setScale(4, RoundingMode.HALF_UP)}",
            modifier = Modifier.padding(start = 12.dp),
            style = MaterialTheme.typography.titleMedium.copy(
                color = MaterialTheme.colorScheme.onBackground,
            ),
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            if (isSuccessful.value) {
                stringResource(R.string.sent_label)
            } else {
                stringResource(R.string.recipient_label)
            },
            style = MaterialTheme.typography.bodySmall.copy(
                color = MaterialTheme.colorScheme.tertiary
            ),
            modifier = Modifier.padding(horizontal = 12.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        DesignCard(
            shape = RoundedCornerShape(24.dp),
            contentPadding = PaddingValues(14.dp),
            color = MaterialTheme.colorScheme.surfaceVariant
        ) {
            DesignDetailLayout(
                lead = {
                    DesignAvatar {
                        DesignAsyncImage(
                            label = username,
                            imageUrl = imageUrl,
                            size = 36.dp,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = MaterialTheme.colorScheme.tertiary,
                            ),
                            color = MaterialTheme.colorScheme.background,
                            modifier = Modifier.clip(CircleShape)
                                .wrapContentSize()
                                .clipToBounds()
                                .clickable(role = Role.Button, onClick = onClick)
                        )
                    } },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        "$username $slugTag".annotate(
                            slugTag,
                            style = MaterialTheme.typography.bodySmall.toSpanStyle().copy(
                                color = MaterialTheme.colorScheme.tertiary
                            )
                        ),
                        modifier = Modifier.padding(start = 12.dp)
                            .weight(1f),
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.onBackground,
                        ),
                    )
                    Icon(
                        painter = if (isSuccessful.value) {
                            painterResource(R.drawable.ic_check)
                        } else {
                            painterResource(R.drawable.ic_transfer)
                        },
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                        tint = if (isSuccessful.value) {
                            PeerAppGreen
                        } else {
                            MaterialTheme.colorScheme.surfaceDim
                        }
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        DesignButton(
            onClick = onSubmit,
            isLoading = state.value,
            enabled = !state.value,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = if (isSuccessful.value) {
                    stringResource(R.string.done_label)
                } else {
                    stringResource(R.string.send_label)
                },
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Bold
                )
            )
        }
        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun PreviewTransferSheet() {
    PeerTheme {
        Column {
            TransferSheet(
                state = remember { mutableStateOf(true) },
                isSuccessful = remember { mutableStateOf(true) },
                username = "johnDoe",
                slug = "1234",
                imageUrl = "http://localhost",
                token = BigDecimal(1.0)
            ) {}
            Spacer(modifier = Modifier.height(16.dp))
            TransferSheet(
                state = remember { mutableStateOf(false) },
                isSuccessful = remember { mutableStateOf(false) },
                username = "johnDoe",
                slug = "1234",
                imageUrl = "http://localhost",
                token = BigDecimal(1.0)
            ) {}
        }
    }
}
