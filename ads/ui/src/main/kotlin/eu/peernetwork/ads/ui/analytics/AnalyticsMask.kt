package eu.peernetwork.ads.ui.analytics

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.ads.ui.R
import eu.peernetwork.core.ui.design.luna.DesignSkeleton
import eu.peernetwork.core.ui.theme.DesignTheme
import eu.peernetwork.core.ui.theme.PeerAppGreen

@Composable
fun AnalyticsMask(
    status: Boolean,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val updatedContent by rememberUpdatedState(content)
    Row(modifier = Modifier.fillMaxWidth()
        .then(modifier)
        .clip(RoundedCornerShape(24.dp))
        .background(MaterialTheme.colorScheme.surfaceVariant)
        .padding(8.dp)) {
        Box(
            modifier = Modifier.height(86.dp)
                .aspectRatio(1.2f)
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.background)
        ) { updatedContent() }
        Column(modifier = Modifier.padding(start = 8.dp)
            .padding(end = 6.dp)) {
            Box(
                contentAlignment = Alignment.BottomStart,
                modifier = Modifier.fillMaxWidth()
                    .heightIn(min = 24.dp)
            ) {
                DesignSkeleton(
                    modifier = Modifier
                        .fillMaxWidth(fraction = .3f)
                        .height(12.dp),
                    color = MaterialTheme.colorScheme.surfaceContainerLowest,
                )
            }
            Box(
                modifier = Modifier.fillMaxWidth()
                    .heightIn(min = 32.dp)
            ) {
                DesignSkeleton(
                    modifier = Modifier
                        .padding(top = 4.dp)
                        .fillMaxWidth(fraction = .6f)
                        .height(12.dp),
                    color = MaterialTheme.colorScheme.surfaceContainerLowest,
                )
            }
            Row(
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
                    .heightIn(min = 24.dp)
            ) {
                if (status) {
                    Box(
                        modifier = Modifier.clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surfaceDim)
                            .padding(vertical = 2.dp)
                            .padding(horizontal = 8.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.active_label),
                            color = PeerAppGreen,
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewAnalyticsMask() {
    DesignTheme(isDarkMode = true) {
        AnalyticsMask(status = true) {}
    }
}
