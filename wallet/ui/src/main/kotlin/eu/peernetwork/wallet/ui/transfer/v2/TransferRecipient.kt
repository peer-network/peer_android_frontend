package eu.peernetwork.wallet.ui.transfer.v2

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.extension.tap
import eu.peernetwork.core.ui.theme.DesignTheme
import eu.peernetwork.wallet.ui.R
import eu.peernetwork.wallet.ui.model.UiRecipient

@Composable
fun TransferRecipient(recipient: UiRecipient) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.surfaceDim)
            .padding(10.dp)
            .padding(vertical = 4.dp)
            .padding(horizontal = 8.dp)
    ) {
        Text(
            text = stringResource(R.string.sending_to),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = buildAnnotatedString {
                append(recipient.username)
                append(" ")
                withStyle(SpanStyle(
                    color = MaterialTheme.colorScheme.outline,
                    fontWeight = FontWeight.Medium,
                    fontSize = MaterialTheme.typography.labelMedium.fontSize
                )) { append("#${recipient.slug}") }
            },
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Composable
fun TransferRecipient(
    recipient: UiRecipient,
    onEdit: () -> Unit,
    onClick: (String) -> Unit,
) {
    val handleClick by rememberUpdatedState(onClick)
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(MaterialTheme.colorScheme.surfaceDim)
            .padding(10.dp)
            .padding(vertical = 4.dp)
    ) {
        Column(modifier = Modifier.weight(1f)
            .padding(start = 8.dp)) {
            Text(
                text = stringResource(R.string.send_to),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = buildAnnotatedString {
                    append(recipient.username)
                    append(" ")
                    withStyle(SpanStyle(
                        color = MaterialTheme.colorScheme.outline,
                        fontWeight = FontWeight.Medium,
                        fontSize = MaterialTheme.typography.labelMedium.fontSize
                    )) { append("#${recipient.slug}") }
                },
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.tap {
                    handleClick(recipient.id)
                }
            )
        }
        Icon(
            painter = painterResource(R.drawable.ic_edit),
            contentDescription = stringResource(R.string.transfer_label),
            tint = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(vertical = 4.dp)
                .size(32.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.background)
                .clickable(onClick = onEdit)
                .padding(8.dp)
        )
    }
}

@Composable
fun TransferRecipient(onClick: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(MaterialTheme.colorScheme.surfaceDim)
            .padding(10.dp)
            .padding(vertical = 4.dp)
    ) {
        Text(
            text = stringResource(R.string.recipient_username),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(top = 8.dp)
                .fillMaxWidth()
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.background)
                .clickable(onClick = onClick)
                .padding(16.dp)
        ) {
            Text(
                text = stringResource(R.string.recipient_search),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.outlineVariant,
                modifier = Modifier.weight(1f)
            )
            Icon(
                painter = painterResource(R.drawable.ic_recipient_search),
                contentDescription = stringResource(R.string.recipient_search),
                tint = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

@Preview
@Composable
fun PreviewTransferRecipient() {
    DesignTheme(isDarkMode = true) {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            val recipient = UiRecipient(
                id = "1",
                username = "constantine",
                slug = "1234",
                imageUrl = "http:localhost"
            )
            TransferRecipient {}
            TransferRecipient(recipient, {}) {}
            TransferRecipient(recipient)
        }
    }
}
