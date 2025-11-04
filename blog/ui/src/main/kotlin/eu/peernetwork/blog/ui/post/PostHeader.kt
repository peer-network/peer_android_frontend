package eu.peernetwork.blog.ui.post

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
fun PostHeader(
    slug: String,
    username: String,
    imageUrl: String,
    modifier: Modifier = Modifier,
    onAuthorClick: () -> Unit,
    connection: @Composable () -> Unit
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
                size = 32.dp,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.clickable(
                    role = Role.Button,
                    onClick = onAuthorClick
                )
            )
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 8.dp)
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
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.outline,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
            )
        }
        updatedConnection()
        PostHeaderOption()
    }
}

@Composable
private fun PostHeaderOption() {
    IconButton(
        onClick = {},
        modifier = Modifier.padding(horizontal = 8.dp)
            .size(28.dp),
        colors = IconButtonColors(
            containerColor = MaterialTheme.colorScheme.surfaceTint,
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
    IconButton(
        onClick = {},
        modifier = Modifier.size(28.dp),
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
        PostHeader(
            slug = "#239100",
            username = "John",
            imageUrl = "http://localhost",
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(vertical = 10.dp),
            onAuthorClick = {}
        ) {
            DesignButton(
                minHeight = 28.dp,
                onClick = {  },
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) { Text("peer") }
        }
    }
}
