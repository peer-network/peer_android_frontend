package eu.peernetwork.media.ui.selector.photo

import android.graphics.Bitmap
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.design.material.DesignThumbnail

@Composable
fun PhotoItem(
    enable: State<Boolean>,
    isSelected: Boolean,
    color: Color,
    thumbnail: String,
    bitmap: State<Bitmap?>,
    onSelect: () -> Unit,
    onLoad: (String) -> Unit,
) {
    Box(modifier = Modifier
        .aspectRatio(1f)
        .background(MaterialTheme.colorScheme.surfaceVariant)
        .clickable(role = Role.Button, onClick = onSelect)) {
        DesignThumbnail(
            enable = enable,
            thumbnail = thumbnail,
            bitmap = bitmap,
            modifier = Modifier.fillMaxWidth()
                .aspectRatio(1f)
                .background(MaterialTheme.colorScheme.surfaceVariant),
            onLoad = onLoad
        )
        Box(modifier = Modifier
            .fillMaxSize()
            .graphicsLayer {
                alpha = if (isSelected) {
                    1f
                } else {
                    0f
                }
            }.drawBehind {
                drawRoundRect(
                    color = color,
                    size = size,
                    style = Stroke(width = 4.dp.toPx())
                )
            }
        )
    }
}
