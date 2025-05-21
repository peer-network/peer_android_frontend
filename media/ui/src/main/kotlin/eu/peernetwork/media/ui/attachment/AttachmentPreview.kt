package eu.peernetwork.media.ui.attachment

import android.annotation.SuppressLint
import android.graphics.Bitmap
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import eu.peernetwork.media.core.model.UiAttachment
import eu.peernetwork.media.ui.R
import eu.peernetwork.media.ui.thumbnail.ThumbnailScreen
import kotlinx.coroutines.flow.StateFlow

@Composable
@SuppressLint("UnusedBoxWithConstraintsScope")
fun AttachmentPreview(
    onEdit: () -> Unit,
    state: StateFlow<Map<String, Bitmap?>>,
    onRefresh: (String) -> Unit,
    attachment: MutableState<UiAttachment>
) {
    val attached = remember(attachment.value) { attachment.value }
    val pagerState = rememberPagerState(initialPage = 0) { attached.files.size }
    BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
        val dimension = maxWidth * 0.5f
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxWidth()
                .padding(vertical = 16.dp),
            pageSize = PageSize.Fixed(dimension)
        ) { page ->
            Box(
                modifier = Modifier
                    .aspectRatio(1f)
                    .padding(start = if (page == 0) {
                        24.dp
                    } else {
                        8.dp
                    }, end = if (page == attached.files.size -1) {
                        24.dp
                    } else {
                        8.dp
                    }, bottom = 24.dp).clip(RoundedCornerShape(24.dp))
                    .background(MaterialTheme.colorScheme.tertiaryContainer)
                    .clickable(onClick = onEdit)
            ) {
                ThumbnailScreen(
                    attached.files[page].thumbnail,
                    state,
                    onRefresh
                )
            }
        }
    }
}

@Composable
@SuppressLint("UnusedBoxWithConstraintsScope")
fun AttachmentPlaceholder(onAttach: () -> Unit) {
    val color = MaterialTheme.colorScheme.tertiaryContainer
    BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
        val dimension = maxWidth * 0.7f
        Box(
            contentAlignment = Alignment.BottomEnd,
            modifier = Modifier
                .wrapContentSize()
                .align(Alignment.Center)
                .padding(8.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(dimension)
                    .padding(16.dp)
                    .drawBehind {
                        val stroke = Stroke(
                            width = 4.dp.toPx(),
                            cap = StrokeCap.Round,
                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(24f, 24f), 0f)
                        )
                        drawRoundRect(
                            color = color,
                            size = size,
                            style = stroke,
                            cornerRadius = androidx.compose.ui.geometry.CornerRadius(24.dp.toPx())
                        )
                    }.clip(RoundedCornerShape(24.dp))
                    .clickable(role = Role.Button, onClick = onAttach)
            ) {
                Text(
                    stringResource(R.string.media_label),
                    color = MaterialTheme.colorScheme.tertiary
                )
            }
            IconButton(
                onClick = onAttach,
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onBackground,
                ),
                modifier = Modifier.size(56.dp)
            ) {
                Icon(
                    painter = painterResource(eu.peernetwork.core.ui.R.drawable.ic_plus),
                    contentDescription = stringResource(R.string.media_label),
                    modifier = Modifier.padding(18.dp)
                )
            }
        }
    }
}
