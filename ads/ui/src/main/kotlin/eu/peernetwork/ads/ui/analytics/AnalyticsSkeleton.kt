package eu.peernetwork.ads.ui.analytics

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.design.luna.DesignSkeleton
import eu.peernetwork.core.ui.theme.DesignTheme

@Composable
fun AnalyticsSkeleton(modifier: Modifier = Modifier) {
    Column {
        DesignSkeleton(modifier = Modifier.padding(top = 8.dp)
            .padding(horizontal = 12.dp)
            .padding(bottom = 12.dp)
            .padding(horizontal = 8.dp)
            .fillMaxWidth(fraction = .3f)
            .height(18.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.surfaceDim))
        Row(modifier = Modifier.fillMaxWidth()
            .padding(horizontal = 16.dp)
            .then(modifier)
            .clip(RoundedCornerShape(24.dp))
            .background(MaterialTheme.colorScheme.surfaceDim)
            .padding(8.dp)) {
            Box(
                modifier = Modifier.height(86.dp)
                    .aspectRatio(1.2f)
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.background)
            )
        }
        DesignSkeleton(modifier = Modifier.padding(top = 12.dp)
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .height(48.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.surfaceDim))
        DesignSkeleton(modifier = Modifier.padding(top = 16.dp)
            .padding(horizontal = 24.dp)
            .fillMaxWidth(fraction = .2f)
            .height(18.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.surfaceDim))
        DesignSkeleton(modifier = Modifier.padding(top = 10.dp)
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .heightIn(min = 84.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(MaterialTheme.colorScheme.surfaceDim)
        )
        DesignSkeleton(modifier = Modifier.padding(top = 10.dp)
            .padding(bottom = 12.dp)
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .height(48.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.surfaceDim))
    }
}

@Preview
@Composable
fun PreviewAnalyticsSkeleton() {
    DesignTheme(isDarkMode = true) {
        AnalyticsSkeleton()
    }
}
