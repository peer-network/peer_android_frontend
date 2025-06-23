package eu.peernetwork.media.ui.attachment

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.graphics.Bitmap
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
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
    attachment: MutableState<UiAttachment>
) {
    val attached = remember(attachment.value) { attachment.value }
    val pagerState = rememberPagerState(initialPage = 0) { attached.files.size + 1 }
    val handleOnLoad by rememberUpdatedState(onLoad)
    val handleOnRefresh by rememberUpdatedState(onRefresh)
    AttachmentPreview(pagerState, onAttach, {
        attachment.value = UiAttachment.File(
            attachment.value.media,
            attachment.value.files - attachment.value.files[it]
        )
    }) { index ->
        DesignThumbnail(
            attached.files[index].thumbnail,
            handleOnLoad(attached.files[index].thumbnail),
        ) { handleOnRefresh(index) }
    }
}

@Composable
@SuppressLint("UnusedBoxWithConstraintsScope")
fun AttachmentPreview(
    state: PagerState,
    onAttach: () -> Unit,
    onRemove: (Int) -> Unit,
    content: @Composable (Int) -> Unit
) {
    val updatedContent by rememberUpdatedState(content)
    val handleOnRemove by rememberUpdatedState(onRemove)
    HorizontalPager(
        state = state,
        modifier = Modifier.fillMaxWidth()
            .padding(vertical = 24.dp),
        pageSpacing = 2.dp,
        contentPadding = PaddingValues(horizontal = 48.dp)
    ) { page ->
        Box(modifier = Modifier
            .aspectRatio(1f)
            .graphicsLayer {
                val pageOffset = (state.currentPage - page + state.currentPageOffsetFraction).absoluteValue
                val scale = lerp(
                    start = 0.9f,
                    stop = 1f,
                    fraction = 1f - pageOffset.coerceIn(0f, 1f)
                )
                scaleX = scale
                scaleY = scale
                shadowElevation = if (scale == 1f) 8.dp.toPx() else 4.dp.toPx()
                translationX = pageOffset
            }.clip(RoundedCornerShape(24.dp))
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
                Box(contentAlignment = Alignment.BottomEnd) {
                    updatedContent(page)
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.padding(16.dp)
                            .size(28.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .padding(6.dp)
                            .clickable(role = Role.Button) {
                                handleOnRemove(page)
                            }
                    ) {
                        Icon(
                            painter = painterResource(eu.peernetwork.core.ui.R.drawable.ic_cancel),
                            contentDescription = stringResource(R.string.remove_label),
                            tint = MaterialTheme.colorScheme.surfaceTint
                        )
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
        val pagerState = rememberPagerState(initialPage = 0) { 2 }
        AttachmentPreview(pagerState, {}, {}) {
            Box(modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surfaceVariant))
        }
    }
}
