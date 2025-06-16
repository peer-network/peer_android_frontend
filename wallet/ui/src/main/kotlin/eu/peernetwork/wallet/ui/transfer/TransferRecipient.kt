package eu.peernetwork.wallet.ui.transfer

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
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
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.design.compose.DesignAsyncImage
import eu.peernetwork.core.ui.design.compose.DesignAvatar
import eu.peernetwork.core.ui.design.compose.DesignCard
import eu.peernetwork.core.ui.design.compose.DesignDetailLayout
import eu.peernetwork.core.ui.extension.annotate
import eu.peernetwork.core.ui.theme.PeerTheme
import eu.peernetwork.wallet.ui.R
import eu.peernetwork.wallet.ui.model.UiRecipient
import java.util.UUID

@Composable
fun TransferRecipient(
    recipient: UiRecipient,
    onClick: () -> Unit,
    icon: @Composable () -> Unit
) {
    val slugTag = "#${recipient.slug}"
    val updatedIcon by rememberUpdatedState(icon)
    DesignCard(
        shape = RoundedCornerShape(24.dp),
        contentPadding = PaddingValues(14.dp),
        color = MaterialTheme.colorScheme.surfaceVariant
    ) {
        DesignDetailLayout(
            lead = {
                DesignAvatar {
                    DesignAsyncImage(
                        label = recipient.username,
                        imageUrl = recipient.imageUrl,
                        size = 36.dp,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.tertiary,
                        ),
                        color = MaterialTheme.colorScheme.background,
                        modifier = Modifier
                            .clip(CircleShape)
                            .wrapContentSize()
                            .clipToBounds()
                            .clickable(role = Role.Button, onClick = onClick)
                    )
                } },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    "${recipient.username} $slugTag".annotate(
                        slugTag,
                        style = MaterialTheme.typography.bodySmall.toSpanStyle().copy(
                            color = MaterialTheme.colorScheme.tertiary
                        )
                    ),
                    modifier = Modifier
                        .padding(start = 12.dp)
                        .weight(1f),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onBackground,
                    ),
                )
                updatedIcon()
            }
        }
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun PreviewTransferRecipient() {
    PeerTheme {
        val recipient = UiRecipient(
            id = UUID.randomUUID().toString(),
            slug = "1234",
            username = "johnDoe",
            imageUrl = "http://localhost"
        )
        TransferRecipient(recipient, {}) {
            Icon(
                painter = painterResource(R.drawable.ic_transfer),
                contentDescription = null,
                modifier = Modifier.size(18.dp),
                tint = MaterialTheme.colorScheme.tertiary
            )
        }
    }
}
