package eu.peernetwork.blog.ui.post

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.blog.ui.R
import eu.peernetwork.core.ui.design.luna.DesignAvatar
import eu.peernetwork.core.ui.design.luna.DesignButton
import eu.peernetwork.core.ui.design.luna.DesignImage
import eu.peernetwork.core.ui.theme.DesignTheme

@Composable
fun PostToolbar(
    slug: String,
    username: String,
    imageUrl: String,
    modifier: Modifier = Modifier,
    pinnedBy: String? = null,
    color: Color = MaterialTheme.colorScheme.surfaceContainerLow,
    accent: Color = MaterialTheme.colorScheme.surfaceVariant,
    onAuthorClick: () -> Unit,
    onMenu: () -> Unit,
    connection: @Composable RowScope.() -> Unit
) {
    val updatedConnection by rememberUpdatedState(connection)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier),
        verticalAlignment = Alignment.CenterVertically
    ) {
        DesignAvatar {
            DesignImage(
                label = username,
                imageUrl = imageUrl,
                size = 36.dp,
                color = color,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onBackground
                ),
                modifier = Modifier.clickable(
                    role = Role.Button,
                    onClick = onAuthorClick
                )
            )
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 10.dp)
        ) {
            Text(
                text = username,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onBackground,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
            )
            Text(
                text = slug,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.outline,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
            )
        }
        updatedConnection()
        PostHeaderOption(
            color = accent,
            pinnedBy = pinnedBy,
            onMenu = onMenu
        )
    }
}

@Composable
private fun PostHeaderOption(
    color: Color = MaterialTheme.colorScheme.surfaceVariant,
    pinnedBy: String? = null,
    onMenu: () -> Unit
) {
    if (pinnedBy != null) {
        IconButton(
            onClick = { },
            modifier = Modifier.padding(horizontal = 8.dp)
                .size(32.dp),
            colors = IconButtonColors(
                containerColor = color,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                disabledContentColor = MaterialTheme.colorScheme.outlineVariant,
                disabledContainerColor = MaterialTheme.colorScheme.surfaceContainerLow
            )
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_pin),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.size(18.dp)
            )
        }
    } else {
        Spacer(modifier = Modifier.width(8.dp))
    }
    IconButton(
        onClick = onMenu,
        modifier = Modifier.size(32.dp),
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_option),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.size(18.dp)
        )
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
fun PreviewPostHeader() {
    DesignTheme(isDarkMode = false) {
        PostToolbar(
            slug = "#239100",
            username = "John",
            imageUrl = "http://localhost",
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(vertical = 8.dp),
            onAuthorClick = {},
            onMenu = {}
        ) {
            DesignButton(
                minHeight = 32.dp,
                onClick = {  },
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) { Text("peer") }
        }
    }
}
