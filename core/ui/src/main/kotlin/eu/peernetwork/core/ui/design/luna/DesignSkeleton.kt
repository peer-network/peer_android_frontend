package eu.peernetwork.core.ui.design.luna

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.theme.DesignTheme

@Composable
fun DesignSkeleton(
    modifier: Modifier = Modifier,
    shape: Shape = CircleShape,
) {
    DesignSkeleton(
        modifier = modifier,
        shape = shape
    ) {}
}

@Composable
fun DesignSkeleton(
    modifier: Modifier = Modifier,
    shape: Shape = CircleShape,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        content = content,
        modifier = modifier.clip(shape)
            .background(MaterialTheme.colorScheme.surfaceDim)
    )
}

@Preview
@Composable
fun DarkDesignSkeletonPreview() {
    DesignTheme(isDarkMode = true) {
        Box(
            modifier = Modifier.fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            DesignSkeleton(modifier = Modifier
                .padding(24.dp)
                .height(48.dp)
                .fillMaxWidth())
        }
    }
}

@Preview
@Composable
fun DesignSkeletonPreview() {
    DesignTheme {
        Box(
            modifier = Modifier.fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            DesignSkeleton(modifier = Modifier
                .padding(24.dp)
                .height(48.dp)
                .fillMaxWidth())
        }
    }
}
