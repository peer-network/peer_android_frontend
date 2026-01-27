package eu.peernetwork.wallet.ui.transactions

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.design.luna.DesignAvatar
import eu.peernetwork.core.ui.theme.DesignTheme
import eu.peernetwork.core.ui.theme.PeerAppDarkRed
import eu.peernetwork.wallet.ui.R

@Composable
fun TransactionsAvatar(
    icon: Painter,
    color: Color,
    content: @Composable () -> Unit
) {
    val updatedContent by rememberUpdatedState(content)
    DesignAvatar(
        icon = {
            Icon(
                painter = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.size(10.dp)
                    .clip(CircleShape)
                    .background(color)
                    .padding(1.dp)
            )
        },
    ) { updatedContent() }
}

@Composable
@Preview
fun PreviewTransactionsAvatar() {
    DesignTheme(isDarkMode = true) {
        TransactionsAvatar(
            icon = painterResource(R.drawable.ic_forward),
            color = MaterialTheme.colorScheme.primary,
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_love),
                contentDescription = null,
                tint = PeerAppDarkRed,
                modifier = Modifier.background(MaterialTheme.colorScheme.background)
                    .padding(8.dp)
            )
        }
    }
}
