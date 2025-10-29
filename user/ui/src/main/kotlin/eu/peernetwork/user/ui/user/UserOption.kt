package eu.peernetwork.user.ui.user

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.design.luna.DesignButton
import eu.peernetwork.core.ui.design.luna.designPrimaryButtonColors
import eu.peernetwork.core.ui.design.luna.designTertiaryButtonColors
import eu.peernetwork.core.ui.theme.DesignTheme
import eu.peernetwork.user.ui.R

@Composable
fun UserOption(
    onShare: () -> Unit,
    content: @Composable () -> Unit,
) {
    val updatedContent by rememberUpdatedState(content)
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(top = 12.dp)
    ) {
        updatedContent()
        DesignButton(
            onClick = onShare,
            minHeight = 42.dp,
            contentPadding = PaddingValues(horizontal = 16.dp),
            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
            colors = designTertiaryButtonColors(),
            modifier = Modifier
                .weight(1f)
                .padding(start = 6.dp),
            trailing = {
                Icon(
                    painter = painterResource(R.drawable.ic_share),
                    contentDescription = stringResource(eu.peernetwork.core.ui.R.string.profile_label),
                    tint = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier
                        .padding(start = 10.dp)
                        .size(16.dp)
                )
            }
        ) { Text(stringResource(R.string.share_label)) }
        IconButton({}) {
            Icon(
                painter = painterResource(R.drawable.ic_menu),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onBackground,
            )
        }
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
fun PreviewUserOption() {
    DesignTheme(isDarkMode = false) {
        Column(modifier = Modifier.padding(24.dp)) {
            UserOption(onShare = {}) {
                DesignButton(
                    onClick = {},
                    minHeight = 42.dp,
                    colors = designPrimaryButtonColors(),
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 6.dp)
                ) { Text("Peer") }
            }
        }
    }
}
