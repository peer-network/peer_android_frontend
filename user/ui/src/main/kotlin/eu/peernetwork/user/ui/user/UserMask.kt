package eu.peernetwork.user.ui.user

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.design.luna.DesignAvatar
import eu.peernetwork.core.ui.design.luna.DesignSkeleton
import eu.peernetwork.core.ui.theme.DesignTheme
import eu.peernetwork.user.ui.R
import eu.peernetwork.user.ui.model.UiMetric
import eu.peernetwork.user.ui.model.UiStatus

@Composable
fun UserMask(
    status: UiStatus,
    isAuthor: Boolean,
    isAccessible: Boolean,
    modifier: Modifier = Modifier,
    mask: @Composable (Boolean) -> Unit,
    content: @Composable () -> Unit
) {
    val updatedMask by rememberUpdatedState(mask)
    val updatedContent by rememberUpdatedState(content)
    Box(modifier = modifier) {
        if (isAuthor) {
            updatedContent()
        } else if (!isAccessible) {
            updatedMask(false)
        } else if (status == UiStatus.VISIBLE) {
            updatedContent()
        } else {
            updatedMask(true)
        }
    }
}

@Composable
fun UserMask(
    metric: UiMetric,
    status: UiStatus,
    isAuthor: Boolean,
    isAccessible: Boolean,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    UserMask(
        status = status,
        isAuthor = isAuthor,
        isAccessible = isAccessible,
        modifier = modifier,
        mask = { isIllegal ->
            if (isIllegal) {
                UserMask(
                    metric = metric,
                    isAuthor = isAuthor,
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {},
                ) {}
            } else {
                // blured overlay
                Box(modifier = Modifier.fillMaxWidth()) {}
            }
        },
        content = content
    )
}

@Composable
fun UserMask(
    metric: UiMetric,
    isAuthor: Boolean,
    modifier: Modifier = Modifier,
    onClick: (UserMetric) -> Unit,
    content: @Composable () -> Unit
) {
    val isVisible = rememberSaveable { mutableStateOf(false) }
    val updatedContent by rememberUpdatedState(content)
    val handleOnClick by rememberUpdatedState(onClick)
    if (isVisible.value) {
        updatedContent()
    } else {
        Column {
            Column(modifier = modifier) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    DesignAvatar {
                        DesignSkeleton(
                            modifier = Modifier.size(56.dp),
                            color = MaterialTheme.colorScheme.surfaceContainerLow,
                        )
                    }
                    Column(modifier = Modifier.padding(start = 16.dp)) {
                        DesignSkeleton(
                            modifier = Modifier.fillMaxWidth(fraction = .3f)
                                .height(12.dp),
                            color = MaterialTheme.colorScheme.surfaceContainerLowest,
                        )
                        UserMetric(
                            overview = metric,
                            labelColor = MaterialTheme.colorScheme.outline,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 6.dp),
                            onClick = {
                                if (!(!isAuthor && it == UserMetric.PEER)) {
                                    handleOnClick(it)
                                }
                            }
                        )
                    }
                }
                DesignSkeleton(
                    modifier = Modifier.padding(top = 16.dp),
                    color = MaterialTheme.colorScheme.surfaceContainerLow,
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.padding(horizontal = 12.dp)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_trash),
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = MaterialTheme.colorScheme.onBackground,
                        )
                        Text(
                            text = stringResource(R.string.illegal_profile),
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.padding(vertical = 6.dp)
                                .padding(end = 8.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
@Preview
fun PreviewUserMask() {
    DesignTheme(isDarkMode = true) {
        val metric = UiMetric(
            posts = 0,
            peers = 0,
            followers = 0,
            followed = 0
        )
        UserMask(
            metric = metric,
            status = UiStatus.ILLEGAL,
            isAuthor = false,
            isAccessible = true,
            modifier = Modifier.padding(16.dp)
                .fillMaxWidth()
                .aspectRatio(1f),
        ) {}
    }
}
