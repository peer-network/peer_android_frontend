package eu.peernetwork.blog.ui.post

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.blog.ui.R
import eu.peernetwork.blog.ui.model.UiStatus
import eu.peernetwork.core.ui.design.luna.DesignOutlineButton
import eu.peernetwork.core.ui.design.luna.DesignSkeleton
import eu.peernetwork.core.ui.theme.DesignTheme

@Composable
fun PostTextMask(
    status: UiStatus,
    isAuthor: Boolean,
    isAccessible: Boolean,
    isVisible: MutableState<Boolean>,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val updatedContent by rememberUpdatedState(content)
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        if (isAuthor) {
            updatedContent()
        } else if (!isAccessible) {
            if (isVisible.value) {
                updatedContent()
            } else {
                PostTextMask { isVisible.value = true }
            }
        } else if (status == UiStatus.VISIBLE) {
            updatedContent()
        } else {
            PostTextMask()
        }
    }
}

@Composable
fun PostTextMask() {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            painter = painterResource(R.drawable.ic_delete),
            contentDescription = stringResource(R.string.hidden_content_label),
            tint = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier
                .size(42.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceContainerLowest)
                .padding(10.dp)
        )
        Text(
            text = stringResource(R.string.illegal_content_description),
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 10.dp)
        )
    }
}

@Composable
fun PostTextMask(onClick: () -> Unit) {
    Row(
        modifier = Modifier.padding(top = 4.dp)
            .padding(horizontal = 8.dp)
            .padding(bottom = 16.dp),
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
                text = stringResource(R.string.hidden_content_description),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.outline,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
        DesignOutlineButton(
            onClick = onClick,
            minHeight = 36.dp,
            contentPadding = PaddingValues(
                horizontal = 24.dp,
                vertical = 8.dp
            ),
        ) { Text(stringResource(R.string.show_label)) }
    }
}

@Composable
fun PostTextMask(
    modifier: Modifier = Modifier,
    isVisible: Boolean,
    fraction: Float = .3f,
    content: @Composable () -> Unit
) {
    val updatedContent by rememberUpdatedState(content)
    Box(modifier) {
        if (!isVisible) {
            DesignSkeleton(
                modifier = Modifier.padding(vertical = 2.dp)
                    .fillMaxWidth(fraction = fraction)
                    .height(12.dp),
                color = MaterialTheme.colorScheme.surfaceContainerLowest,
            )
        } else {
            updatedContent()
        }
    }
}

@Composable
@Preview
fun PreviewPostTextMask() {
    DesignTheme(isDarkMode = true) {
        val isVisible = remember { mutableStateOf(true) }
        PostTextMask(
            status = UiStatus.ILLEGAL,
            isAuthor = false,
            isAccessible = false,
            isVisible = isVisible,
            Modifier.fillMaxWidth()
                .padding(8.dp)
        ) {}
    }
}
