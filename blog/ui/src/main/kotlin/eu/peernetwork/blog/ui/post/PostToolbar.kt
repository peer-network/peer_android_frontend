package eu.peernetwork.blog.ui.post

import androidx.compose.foundation.Image
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import eu.peernetwork.blog.ui.model.UiStatus
import eu.peernetwork.core.ui.design.luna.DesignAvatar
import eu.peernetwork.core.ui.design.luna.DesignButton
import eu.peernetwork.core.ui.design.luna.DesignImage
import eu.peernetwork.core.ui.extension.tap
import eu.peernetwork.core.ui.theme.DesignTheme

@Composable
fun PostToolbar(
    slug: String,
    username: String,
    imageUrl: String,
    isVisible: MutableState<Boolean>,
    modifier: Modifier = Modifier,
    pinnedBy: String? = null,
    status: UiStatus,
    isAuthor: Boolean,
    isAccessible: Boolean,
    color: Color = MaterialTheme.colorScheme.surfaceContainerLow,
    onAuthorClick: () -> Unit,
    onMenu: () -> Unit,
    connection: @Composable RowScope.() -> Unit
) {
    PostToolbarMask(
        status = status,
        isAuthor = isAuthor,
        isAccessible = isAccessible,
        isVisible = isVisible,
        pinnedBy = pinnedBy,
        onMenu = onMenu,
        onClick = onAuthorClick,
        connection = connection,
    ) {
        PostToolbar(
            slug = slug,
            username = username,
            imageUrl = imageUrl,
            modifier = modifier,
            pinnedBy = pinnedBy,
            color = color,
            onAuthorClick = onAuthorClick,
            onMenu = onMenu,
            connection = connection
        )
    }
}

@Composable
fun PostToolbar(
    slug: String,
    username: String,
    imageUrl: String,
    modifier: Modifier = Modifier,
    pinnedBy: String? = null,
    color: Color = MaterialTheme.colorScheme.surfaceContainerLow,
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
                modifier = Modifier.tap(onAuthorClick)
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
        PostToolbarOption(
            pinnedBy = pinnedBy,
            onMenu = onMenu
        )
    }
}

@Composable
fun PostToolbarOption(
    pinnedBy: String? = null,
    onMenu: () -> Unit
) {
    if (pinnedBy != null) {
        Image(
            painter = painterResource(R.drawable.ic_pinned),
            contentDescription = null,
            modifier = Modifier.padding(horizontal = 8.dp)
                .size(28.dp),
        )
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
@Preview
fun PreviewPostHeader() {
    DesignTheme(isDarkMode = true) {
        val isVisible = remember { mutableStateOf(false) }
        PostToolbar(
            slug = "#239100",
            username = "John",
            imageUrl = "http://localhost",
            status = UiStatus.VISIBLE,
            isAuthor = true,
            isAccessible = true,
            isVisible = isVisible,
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
