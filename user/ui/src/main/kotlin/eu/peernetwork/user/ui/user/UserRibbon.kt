package eu.peernetwork.user.ui.user

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.theme.DesignTheme
import eu.peernetwork.user.ui.R
import eu.peernetwork.user.ui.model.UiAccount
import eu.peernetwork.user.ui.model.UiMetric
import eu.peernetwork.user.ui.model.UiStatus

@Composable
fun UserRibbon(
    painter: Painter,
    title: String,
) {
    Box(
        modifier = Modifier.fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceContainerLow),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(vertical = 8.dp)
                .padding(horizontal = 20.dp)
        ) {
            Icon(
                painter = painter,
                contentDescription = title,
                tint = MaterialTheme.colorScheme.outline,
                modifier = Modifier.size(16.dp)
            )
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.outline,
                modifier = Modifier.padding(horizontal = 6.dp)
            )
        }
    }
}

@Composable
fun UserStatusRibbon(account: UiAccount) {
    if (account.status == UiStatus.ILLEGAL) {
        UserRibbon(
            painter = painterResource(R.drawable.ic_trash),
            title = stringResource(R.string.removed_profile_message)
        )
    } else if (!account.isAccessible) {
        UserRibbon(
            painter = painterResource(R.drawable.ic_eye_closed),
            title = stringResource(R.string.hidden_profile_message)
        )
    }
}

@Preview
@Composable
fun PreviewUserRibbon() {
    DesignTheme(isDarkMode = true) {
        val account = UiAccount(
            id = System.currentTimeMillis().toString(),
            username = "John Doe",
            slug = 2343,
            bio = "Description....",
            imageUrl = "",
            metric = UiMetric(
                posts = 0,
                peers = 0,
                followers = 0,
                followed = 0
            ),
            isFollowing = false,
            isFollowed = false,
            reported = true,
            isAccessible = true,
            status = UiStatus.VISIBLE
        )
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            UserStatusRibbon(account)
            UserStatusRibbon(account.copy(status = UiStatus.ILLEGAL))
            UserStatusRibbon(account.copy(
                isAccessible = false,
                status = UiStatus.HIDDEN
            ))
        }
    }
}
