package eu.peernetwork.blog.ui.gallery

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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
import eu.peernetwork.core.ui.theme.DesignTheme

@Composable
fun GalleryToolbarMask(
    status: UiStatus,
    isAuthor: Boolean,
    isAccessible: Boolean,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val updatedContent by rememberUpdatedState(content)
    val isVisible = remember { mutableStateOf(false) }
    Box(modifier = modifier) {
        if (status == UiStatus.ILLEGAL) {
            GalleryToolbarMask()
        } else if (!isAccessible && !isAuthor) {
            if (isVisible.value) {
                updatedContent()
            } else {
                GalleryToolbarMask { isVisible.value = true }
            }
        } else {
            updatedContent()
        }
    }
}

@Composable
fun GalleryToolbarMask(onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
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
    }
}

@Composable
fun GalleryToolbarMask() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
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
        Text(
            text = stringResource(R.string.illegal_username),
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 10.dp)
        )
    }
}

@Composable
@Preview
fun PreviewCommentMask() {
    DesignTheme(isDarkMode = true) {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            GalleryToolbarMask(
                status = UiStatus.HIDDEN,
                isAuthor = false,
                isAccessible = false,
            ) {}
            GalleryToolbarMask(
                status = UiStatus.ILLEGAL,
                isAuthor = false,
                isAccessible = false,
            ) {}
        }
    }
}
