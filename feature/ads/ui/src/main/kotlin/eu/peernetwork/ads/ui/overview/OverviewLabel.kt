package eu.peernetwork.ads.ui.overview

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.theme.DesignTheme
import eu.peernetwork.feature.ads.ui.R

@Composable
fun OverviewLabel(
    modifier: Modifier = Modifier,
    lead: @Composable () -> Unit,
    trailing: @Composable () -> Unit
) {
    val updatedLead by rememberUpdatedState(lead)
    val updatedTrailing by rememberUpdatedState(trailing)
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        updatedLead()
        updatedTrailing()
    }
}

@Composable
fun OverviewLabel(
    label: String,
    modifier: Modifier = Modifier,
    padding: PaddingValues = PaddingValues(
        horizontal = 24.dp,
        vertical = 12.dp
    ),
    content: @Composable () -> Unit
) {
    val updatedContent by rememberUpdatedState(content)
    val start = with(LocalDensity.current) { 56.dp.toPx() }
    val verticalGradient = Brush.linearGradient(
        start = Offset(start, 0f),
        colors = listOf(
            MaterialTheme.colorScheme.primary,
            MaterialTheme.colorScheme.secondaryContainer,
        ),
        end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
    )
    OverviewLabel(
        modifier = Modifier.then(modifier)
            .clip(CircleShape)
            .background(brush = verticalGradient)
            .padding(padding),
        lead = {
            Text(
                text = label,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
        },
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(R.drawable.ic_diamond),
                contentDescription = null,
                modifier = Modifier.padding(end = 4.dp)
                    .size(24.dp),
                tint = MaterialTheme.colorScheme.onBackground
            )
            CompositionLocalProvider(
                LocalTextStyle provides MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            ) { updatedContent() }
        }
    }
}

@Composable
fun OverviewLabel(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.surfaceVariant,
    padding: PaddingValues = PaddingValues(
        horizontal = 24.dp,
        vertical = 12.dp
    )
) {
    OverviewLabel(
        modifier = Modifier.clip(CircleShape)
            .background(color)
            .padding(padding)
            .then(modifier),
        lead = {
            Text(
                text = label,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
        },
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = value,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Icon(
                painter = painterResource(R.drawable.ic_icon),
                contentDescription = null,
                modifier = Modifier.padding(start = 4.dp)
                    .size(24.dp),
                tint = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}

@Preview
@Composable
fun PreviewPostHeader() {
    DesignTheme(isDarkMode = true) {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            OverviewLabel(
                label = "Spendings",
                value = "12",
                modifier = Modifier.fillMaxWidth()
            )
            OverviewLabel(
                label = "Spendings",
                modifier = Modifier.fillMaxWidth()
            ) { Text("12") }
        }
    }
}
