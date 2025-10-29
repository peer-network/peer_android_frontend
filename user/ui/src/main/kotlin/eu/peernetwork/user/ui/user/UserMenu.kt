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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.design.luna.DesignButton
import eu.peernetwork.core.ui.design.luna.designSecondaryButtonColors
import eu.peernetwork.core.ui.design.luna.designTertiaryButtonColors
import eu.peernetwork.core.ui.theme.DesignTheme
import eu.peernetwork.user.ui.R

@Composable
fun UserMenu(
    onInvite: () -> Unit,
    onSettings: () -> Unit,
) {
    val style =  MaterialTheme.typography.bodySmall
        .copy(fontWeight = FontWeight.Bold)
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(top = 12.dp)
    ) {
        DesignButton(
            onClick = onInvite,
            minHeight = 42.dp,
            colors = designSecondaryButtonColors(),
            style = style,
            contentPadding = PaddingValues(horizontal = 16.dp),
            modifier = Modifier.weight(1f)
                .padding(end = 6.dp)
        ) { Text(stringResource(R.string.invite_label)) }
        DesignButton(
            onClick = onSettings,
            minHeight = 42.dp,
            contentPadding = PaddingValues(horizontal = 16.dp),
            style = style,
            colors = designTertiaryButtonColors(),
            modifier = Modifier.weight(1f)
                .padding(start = 6.dp),
            trailing = {
                Icon(
                    painter = painterResource(R.drawable.ic_settings),
                    contentDescription = stringResource(R.string.settings_label),
                    tint = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(start = 10.dp)
                        .size(16.dp)
                )
            }
        ) { Text(stringResource(R.string.settings_label)) }
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
fun PreviewUserMenu() {
    DesignTheme(isDarkMode = false) {
        Column(modifier = Modifier.padding(24.dp)) {
            UserMenu(
                onInvite = {},
                onSettings = {}
            )
        }
    }
}
