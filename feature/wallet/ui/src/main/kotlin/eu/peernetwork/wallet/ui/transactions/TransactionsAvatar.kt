package eu.peernetwork.wallet.ui.transactions

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.design.luna.DesignAvatar
import eu.peernetwork.core.ui.design.luna.DesignImage
import eu.peernetwork.core.ui.theme.DesignTheme
import eu.peernetwork.core.ui.theme.PeerAppDarkRed
import eu.peernetwork.feature.wallet.ui.R
import eu.peernetwork.wallet.ui.model.UiStatus
import eu.peernetwork.wallet.ui.model.UiUser
import kotlin.Boolean

@Composable
fun TransactionsAvatar(
    icon: Painter,
    contentDescription: String? = null,
    isRecipient: Boolean = false,
    content: @Composable () -> Unit
) {
    val updatedContent by rememberUpdatedState(content)
    DesignAvatar(
        icon = {
            Icon(
                painter = icon,
                contentDescription = contentDescription,
                tint = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.size(12.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(1.dp)
                    .graphicsLayer {
                        rotationZ = if (isRecipient) 180f else 0f
                    }
            )
        },
    ) { updatedContent() }
}

@Composable
fun TransactionsProfile(
    user: UiUser,
    isVisible: MutableState<Boolean>,
    onClick: () -> Unit
) {
    if (user.status == UiStatus.ILLEGAL) {
        DesignAvatar {
            Icon(
                painter = painterResource(R.drawable.ic_report),
                contentDescription = stringResource(R.string.illegal_content_description),
                tint = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .padding(10.dp)
                    .size(16.dp)
                    .clip(CircleShape)
            )
        }
    } else if (!user.isAccessible) {
        if (isVisible.value) {
            TransactionsProfile(
                user = user,
                onClick = onClick
            )
        } else {
            DesignAvatar {
                Icon(
                    painter = painterResource(R.drawable.ic_hidden),
                    contentDescription = stringResource(R.string.hidden_content_description),
                    tint = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.background)
                        .padding(10.dp)
                        .size(16.dp)
                        .clip(CircleShape)
                        .clickable { isVisible.value = true }
                )
            }
        }
    } else {
        TransactionsProfile(
            user = user,
            onClick = onClick
        )
    }
}

@Composable
fun TransactionsProfile(
    user: UiUser,
    onClick: () -> Unit
) {
    DesignAvatar {
        DesignImage(
            label = user.username,
            imageUrl = user.imageUrl,
            size = 36.dp,
            color = MaterialTheme.colorScheme.background,
            style = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.onBackground
            ),
            modifier = Modifier.clickable(
                role = Role.Button,
                onClick = onClick
            )
        )
    }
}

@Composable
@Preview
fun PreviewTransactionsAvatar() {
    DesignTheme(isDarkMode = true) {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            val isVisible = remember { mutableStateOf(false) }
            TransactionsAvatar(
                icon = painterResource(R.drawable.ic_transfer_direction)
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_love),
                    contentDescription = null,
                    tint = PeerAppDarkRed,
                    modifier = Modifier.background(MaterialTheme.colorScheme.background)
                        .padding(8.dp)
                )
            }
            TransactionsProfile(
                user = UiUser(
                    id = "123",
                    username = "test",
                    slug = 1234,
                    imageUrl = "http:localhost",
                    isAccessible = false,
                    status = UiStatus.ILLEGAL
                ),
                isVisible = isVisible,
            ) {}
            TransactionsProfile(
                user = UiUser(
                    id = "123",
                    username = "test",
                    slug = 1234,
                    imageUrl = "http:localhost",
                    isAccessible = false,
                    status = UiStatus.VISIBLE
                ),
                isVisible = isVisible,
            ) {}
        }
    }
}
