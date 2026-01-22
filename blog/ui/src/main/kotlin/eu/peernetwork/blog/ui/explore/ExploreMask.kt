package eu.peernetwork.blog.ui.explore

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.blog.ui.R
import eu.peernetwork.blog.ui.model.UiStatus
import eu.peernetwork.core.ui.theme.DesignTheme

@Composable
fun ExploreMask(
    status: UiStatus,
    isAuthor: Boolean,
    isAccessible: Boolean,
    modifier: Modifier = Modifier,
    placeholder: @Composable () -> Unit,
    content: @Composable () -> Unit
) {
    val updatedPlaceholder by rememberUpdatedState(placeholder)
    val updatedContent by rememberUpdatedState(content)
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        if (status == UiStatus.ILLEGAL) {
            ExploreMask()
        } else if (!isAccessible && !isAuthor) {
            updatedPlaceholder()
            Icon(
                painter = painterResource(R.drawable.ic_hidden),
                contentDescription = stringResource(R.string.hidden_content_label),
                tint = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.size(36.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceContainerLowest)
                    .padding(8.dp)
            )
        } else {
            updatedContent()
        }
    }
}

@Composable
fun ExploreMask() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_delete),
            contentDescription = stringResource(R.string.illegal_content_description),
            tint = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.size(36.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceContainerLowest)
                .padding(8.dp)
        )
    }
}

@Composable
@Preview
fun PreviewExploreMask() {
    DesignTheme(isDarkMode = true) {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)){
            ExploreMask(
                status = UiStatus.ILLEGAL,
                isAuthor = false,
                isAccessible = false,
                Modifier.fillMaxWidth()
                    .aspectRatio(1f),
                placeholder = { Box(modifier = Modifier.size(400.dp)) }
            ) {
            }
            ExploreMask(
                status = UiStatus.HIDDEN,
                isAuthor = false,
                isAccessible = false,
                Modifier.fillMaxWidth()
                    .aspectRatio(1f),
                placeholder = { Box(modifier = Modifier.size(400.dp)) }
            ) {}
        }
    }
}
