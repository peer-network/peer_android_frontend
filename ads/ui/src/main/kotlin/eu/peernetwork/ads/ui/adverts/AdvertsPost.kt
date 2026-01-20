package eu.peernetwork.ads.ui.adverts

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
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.ads.ui.R
import eu.peernetwork.core.ui.design.luna.DesignRichText
import eu.peernetwork.core.ui.theme.DesignTheme
import eu.peernetwork.core.ui.theme.PeerAppGreen

@Composable
fun AdvertsPost(
    title: AnnotatedString,
    description: AnnotatedString,
    from: String,
    to: String,
    isActive: Boolean,
    modifier: Modifier = Modifier,
    onSelect: () -> Unit,
    onClick: (DesignRichText, String) -> Unit,
    label: (@Composable () -> Unit)?,
    content: @Composable () -> Unit
) {
    val updatedLabel by rememberUpdatedState(label)
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
                DesignRichText(
                    text = title,
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    onClick = onClick,
                    onTap = onSelect
                )
            }
            if (description.isNotBlank()) {
                Box(
                    modifier = Modifier.fillMaxWidth()
                        .heightIn(min = 32.dp)
                ) {
                    DesignRichText(
                        text = description,
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.labelMedium,
                        maxLines = 2,
                        modifier = Modifier.padding(top = 2.dp),
                        onClick = onClick,
                        onTap = onSelect
                    )
                }
            }
            updatedLabel?.let {
                Box(
                    modifier = Modifier.padding(top = 6.dp)
                ) { it() }
            }
            Row(
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
                    .heightIn(min = 24.dp)
            ) {
                Text(
                    text = "$from - $to",
                    color = MaterialTheme.colorScheme.outline,
                    style = MaterialTheme.typography.labelMedium
                )
                if (isActive) {
                    Box(
                        modifier = Modifier.padding(start = 6.dp)
                            .clip(CircleShape)
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
fun PreviewAdvertsPost() {
    DesignTheme(isDarkMode = true) {
        AdvertsPost(
            title = buildAnnotatedString { append("Title") },
            description = buildAnnotatedString { append("There’s something about hiking that resets everything. ") },
            from = "8 Jun 2025",
            to = "10 Jun 2025",
            isActive = true,
            onClick = { _,_ -> },
            label = { AdvertsVisibilityLabel() },
            onSelect = {}
        ) {}
    }
}