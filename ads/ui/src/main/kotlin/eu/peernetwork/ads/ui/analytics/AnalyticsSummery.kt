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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.theme.DesignTheme

@Composable
fun AnalyticsSummery(
    title: AnnotatedString,
    description: AnnotatedString,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val updatedContent by rememberUpdatedState(content)
    Row(modifier = Modifier.fillMaxWidth()
        .then(modifier)
        .clip(RoundedCornerShape(24.dp))
        .background(MaterialTheme.colorScheme.surfaceDim)
        .padding(10.dp)) {
        Box(
            modifier = Modifier.height(86.dp)
                .aspectRatio(1.2f)
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.background)
        ) { updatedContent() }
        Column(modifier = Modifier.padding(start = 12.dp)
            .padding(end = 6.dp)) {
            Box(
                contentAlignment = Alignment.BottomStart,
                modifier = Modifier.fillMaxWidth()
                    .heightIn(min = 24.dp)
            ) {
                Text(
                    text = title,
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1
                )
            }
            Box(
                modifier = Modifier.fillMaxWidth()
                    .heightIn(min = 32.dp)
            ) {
                Text(
                    text = description,
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.labelMedium,
                    maxLines = 2,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewAnalyticsSummery() {
    DesignTheme(isDarkMode = true) {
        AnalyticsSummery(
            title = buildAnnotatedString { append("Title") },
            description = buildAnnotatedString { append("There’s something about hiking that resets everything. ") },
            modifier = Modifier.padding(12.dp)
        ) {}
    }
}
