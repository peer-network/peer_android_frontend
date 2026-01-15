package eu.peernetwork.blog.ui.post

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.blog.ui.R
import eu.peernetwork.blog.ui.model.UiStatus
import eu.peernetwork.core.ui.design.luna.DesignSkeleton
import eu.peernetwork.core.ui.theme.DesignTheme

@Composable
fun PostToolbarMask(
    status: UiStatus,
    isAuthor: Boolean,
    isAccessible: Boolean,
    modifier: Modifier = Modifier,
    pinnedBy: String? = null,
    onMenu: () -> Unit,
    connection: @Composable RowScope.() -> Unit,
    content: @Composable () -> Unit
) {
    val updatedContent by rememberUpdatedState(content)
    val isVisible = remember { mutableStateOf(false) }
    Box(modifier = modifier) {
        if (isAuthor) {
            updatedContent()
        } else if (!isAccessible) {
            if (isVisible.value) {
                updatedContent()
            } else {
                PostToolbarMask(
                    pinnedBy = pinnedBy,
                    onMenu = onMenu,
                    onClick = { isVisible.value = true },
                    connection = connection
                )
            }
        } else if (status == UiStatus.VISIBLE) {
            updatedContent()
        } else {
            PostToolbarMask(
                pinnedBy = pinnedBy,
                onMenu = onMenu,
                connection = connection
            )
        }
    }
}

@Composable
fun PostToolbarMask(
    pinnedBy: String? = null,
    onMenu: () -> Unit,
    onClick: () -> Unit,
    connection: @Composable RowScope.() -> Unit,
) {
    val updatedConnection by rememberUpdatedState(connection)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_hidden),
            contentDescription = stringResource(R.string.hidden_content_label),
            tint = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceContainerLowest)
                .padding(10.dp)
        )
        Column(modifier = Modifier
            .weight(1f)
            .padding(horizontal = 10.dp)) {
            Text(
                text = stringResource(R.string.hidden_content_label),
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = stringResource(R.string.click_to_see_label),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.outline,
                modifier = Modifier.clickable(onClick = onClick)
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
fun PostToolbarMask(
    pinnedBy: String? = null,
    onMenu: () -> Unit,
    connection: @Composable RowScope.() -> Unit,
) {
    val updatedConnection by rememberUpdatedState(connection)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_delete),
            contentDescription = stringResource(R.string.hidden_content_label),
            tint = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceContainerLowest)
                .padding(10.dp)
        )
        Column(modifier = Modifier
            .weight(1f)
            .padding(horizontal = 10.dp)) {
            Text(
                text = stringResource(R.string.illegal_username),
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold
            )
            DesignSkeleton(
                modifier = Modifier
                    .padding(top = 2.dp)
                    .fillMaxWidth(fraction = .3f)
                    .height(8.dp),
                color = MaterialTheme.colorScheme.surfaceContainerLowest,
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
@Preview
fun PreviewPostToolbarMask() {
    DesignTheme(isDarkMode = true) {
        Column {
            PostToolbarMask(
                status = UiStatus.HIDDEN,
                isAuthor = false,
                isAccessible = false,
                onMenu = {},
                connection = {}
            ) {}
            PostToolbarMask(
                pinnedBy = "JohnDoe",
                onMenu = { },
                connection = { }
            )
        }
    }
}
