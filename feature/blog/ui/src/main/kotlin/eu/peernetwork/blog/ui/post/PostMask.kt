package eu.peernetwork.blog.ui.post

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import eu.peernetwork.blog.ui.model.UiStatus
import eu.peernetwork.core.ui.design.luna.DesignOutlineButton
import eu.peernetwork.core.ui.theme.DesignTheme
import eu.peernetwork.feature.blog.ui.R

@Composable
fun PostMask(
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
        if (status == UiStatus.ILLEGAL) {
            PostMask()
        } else if (!isAccessible && !isAuthor) {
            if (isVisible.value) {
                updatedContent()
            } else {
                PostMask { isVisible.value = true }
            }
        } else {
            updatedContent()
        }
    }
}

@Composable
fun PostMask() {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_delete),
            contentDescription = stringResource(R.string.illegal_content_description),
            tint = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.size(48.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceContainerLowest)
                .padding(12.dp)
        )
        Text(
            text = stringResource(R.string.illegal_content_description),
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.labelLarge,
            textAlign = TextAlign.Center,
            lineHeight = 20.sp,
        )
    }
}

@Composable
fun PostMask(onClick: () -> Unit) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_hidden),
            contentDescription = stringResource(R.string.hidden_content_label),
            tint = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.size(48.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceContainerLowest)
                .padding(12.dp)
        )
        Text(
            text = stringResource(R.string.hidden_content_label),
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            text = stringResource(R.string.hidden_content_description),
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.labelLarge,
            textAlign = TextAlign.Center,
        )
        DesignOutlineButton(
            onClick = onClick,
            minHeight = 42.dp,
            contentPadding = PaddingValues(
                horizontal = 24.dp,
                vertical = 8.dp
            ),
            modifier = Modifier.padding(top = 8.dp)
        ) { Text(stringResource(R.string.hidden_content_action)) }
    }
}

@Composable
@Preview
fun PreviewPostMask() {
    DesignTheme(isDarkMode = true) {
        val isVisible = remember { mutableStateOf(false) }
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)){
            PostMask(
                status = UiStatus.ILLEGAL,
                isAuthor = false,
                isAccessible = false,
                isVisible = isVisible,
                Modifier.fillMaxWidth()
                    .aspectRatio(1f)) {
            }
            PostMask(
                status = UiStatus.HIDDEN,
                isAuthor = false,
                isAccessible = false,
                isVisible = isVisible,
                Modifier.fillMaxWidth()
                    .aspectRatio(1f)) {
            }
        }
    }
}
