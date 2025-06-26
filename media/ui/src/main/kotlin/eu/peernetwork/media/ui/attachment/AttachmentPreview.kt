package eu.peernetwork.media.ui.attachment

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.graphics.Bitmap
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import eu.peernetwork.core.ui.design.compose.DesignThumbnail
import eu.peernetwork.core.ui.theme.PeerTheme
import eu.peernetwork.media.core.model.UiAttachment
import eu.peernetwork.media.ui.R
import kotlin.math.absoluteValue

@Composable
@SuppressLint("UnusedBoxWithConstraintsScope")
fun AttachmentPreview(
    onAttach: () -> Unit,
    onLoad: (String) -> Bitmap?,
    onRefresh: (Int) -> Unit,
    onRemove: (Int) -> Unit,
    onPreview: (Int) -> Unit,
    attachment: MutableState<UiAttachment>,
    onSelect: (Int) -> Unit,
) {
    val attached = remember(attachment.value) { attachment.value }
    val handleOnLoad by rememberUpdatedState(onLoad)
    val handleOnRefresh by rememberUpdatedState(onRefresh)
    val handleOnSelect by rememberUpdatedState(onSelect)
    val enabled = remember { derivedStateOf { attached.files.isNotEmpty() } }
    val pagerState = rememberPagerState(initialPage = 0) { attached.files.size + 1 }
    AttachmentPreview(
        state = pagerState,
        enabled = enabled,
        onAttach = onAttach,
        onRemove = onRemove,
        onPreview = onPreview,
    ) { index ->
        DesignThumbnail(
            attached.files[index].thumbnail,
            handleOnLoad(attached.files[index].thumbnail),
        ) { handleOnRefresh(index) }
    }
    LaunchedEffect(pagerState.currentPage) {
        if (pagerState.currentPage != pagerState.pageCount - 1) {
            handleOnSelect(pagerState.currentPage)
        }
    }
}

@Composable
@SuppressLint("UnusedBoxWithConstraintsScope")
fun AttachmentPreview(
    state: PagerState,
    enabled: State<Boolean>,
    onAttach: () -> Unit,
    onRemove: (Int) -> Unit,
    onPreview: (Int) -> Unit,
    content: @Composable (Int) -> Unit
) {
    val updatedContent by rememberUpdatedState(content)
    val handleOnRemove by rememberUpdatedState(onRemove)
    val handleOnPreview by rememberUpdatedState(onPreview)
    HorizontalPager(
        state = state,
        modifier = Modifier.fillMaxWidth(),
        pageSpacing = 2.dp,
        contentPadding = PaddingValues(horizontal = 56.dp)
    ) { page ->
        if (page == state.pageCount - 1 && !enabled.value) {
            AttachmentPlaceholder(
                modifier = Modifier.aspectRatio(.95f),
                onAttach = onAttach
            )
        } else {
            Box(
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .aspectRatio(1f)
                    .graphicsLayer {
                        val pageOffset =
                            (state.currentPage - page + state.currentPageOffsetFraction).absoluteValue
                        val scale = lerp(
                            start = 0.9f,
                            stop = 1f,
                            fraction = 1f - pageOffset.coerceIn(0f, 1f)
                        )
                        scaleX = scale
                        scaleY = scale
                        shadowElevation = if (scale == 1f) 16.dp.toPx() else 8.dp.toPx()
                        translationX = pageOffset
                    }
                    .clip(RoundedCornerShape(24.dp))
            ) {
                if (page == state.pageCount - 1) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .clickable(role = Role.Button, onClick = onAttach)
                    ) {
                        Text(
                            stringResource(R.string.media_label),
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = MaterialTheme.colorScheme.surfaceDim
                            )
                        )
                    }
                } else {
                    Box(
                        contentAlignment = Alignment.BottomEnd,
                        modifier = Modifier.clickable(
                            enabled = true,
                            role = Role.Button
                        ) { handleOnPreview(page) }
                    ) {
                        updatedContent(page)
                        Row(
                            modifier = Modifier
                                .padding(12.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.background)
                                .padding(8.dp)
                                .clickable(
                                    enabled = true,
                                    role = Role.Button
                                ) { handleOnRemove(page) }
                        ) {
                            Icon(
                                painter = painterResource(eu.peernetwork.core.ui.R.drawable.ic_cancel),
                                contentDescription = stringResource(R.string.remove_label),
                                tint = MaterialTheme.colorScheme.tertiary,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun PreviewAttachmentPreview() {
    PeerTheme {
        Column {
            AttachmentPreview(
                rememberPagerState(initialPage = 0) { 1 },
                remember { mutableStateOf(false) },
                {},
                {},
                {}
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
            AttachmentPreview(
                rememberPagerState(initialPage = 0) { 2 },
                remember { mutableStateOf(true) },
                {},
                {},
                {}
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                )
            }
        }
    }
}
