package eu.peernetwork.ads.ui.overview

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import eu.peernetwork.core.ui.theme.DesignTheme

@Composable
fun OverviewSkeleton() {
    Column(modifier = Modifier.fillMaxWidth()
        .padding(horizontal = 16.dp)
        .padding(vertical = 12.dp)) {
        Box(modifier = Modifier.padding(bottom = 10.dp)
            .fillMaxWidth()
            .height(48.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.surfaceDim))
        Box(modifier = Modifier.fillMaxWidth()
            .height(48.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.surfaceDim))
        Box(modifier = Modifier.padding(top = 18.dp)
            .padding(horizontal = 8.dp)
            .fillMaxWidth(fraction = .3f)
            .height(18.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.surfaceDim))
        Box(modifier = Modifier.padding(top = 14.dp)
            .fillMaxWidth()
            .heightIn(min = 80.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(MaterialTheme.colorScheme.surfaceDim)
        )
    }
}

@Preview
@Composable
fun PreviewOverviewSkeleton() {
    DesignTheme(isDarkMode = true) {
        OverviewSkeleton()
    }
}
