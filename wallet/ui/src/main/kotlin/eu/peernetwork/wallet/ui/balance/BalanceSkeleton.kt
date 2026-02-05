package eu.peernetwork.wallet.ui.balance

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.design.luna.DesignSkeleton
import eu.peernetwork.core.ui.theme.DesignTheme

@Composable
@SuppressLint("UnusedBoxWithConstraintsScope")
fun BalanceSkeleton(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(28.dp),
) {
    BoxWithConstraints {
        val vertical = with(LocalDensity.current) { maxHeight.toPx() * .15f }
        val horizontal = with(LocalDensity.current) { maxHeight.toPx() * .1f }
        val verticalGradient = Brush.linearGradient(
            start = Offset(0f, vertical),
            colors = listOf(
                MaterialTheme.colorScheme.surfaceDim,
                MaterialTheme.colorScheme.surfaceDim,
                MaterialTheme.colorScheme.primary.copy(alpha = .5f)
            ),
            end = Offset(horizontal, Float.POSITIVE_INFINITY)
        )
        Row(
            modifier = Modifier.then(modifier)
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(MaterialTheme.colorScheme.surfaceDim)
                .background(brush = verticalGradient)
                .padding(contentPadding),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            DesignSkeleton(
                modifier.fillMaxWidth(fraction = .4f)
                    .height(24.dp),
                color = MaterialTheme.colorScheme.onBackground,
            )
            DesignSkeleton(
                modifier.padding(start = 16.dp)
                    .size(36.dp)
                    .height(36.dp),
                color = MaterialTheme.colorScheme.onBackground,
            )
        }
    }
}

@Preview
@Composable
fun PreviewBalanceSkeleton() {
    DesignTheme(isDarkMode = true) {
        BalanceSkeleton()
    }
}
