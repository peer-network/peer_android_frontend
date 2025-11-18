package eu.peernetwork.ads.ui.boost

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.ads.ui.R
import eu.peernetwork.core.ui.theme.DesignTheme

@Composable
fun BoostLabel(
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
fun BoostLabel(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.surfaceContainerLowest,
    padding: PaddingValues = PaddingValues(
        horizontal = 24.dp,
        vertical = 14.dp
    )
) {
    BoostLabel(
        modifier = Modifier
            .then(modifier)
            .clip(CircleShape)
            .background(color)
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
fun PreviewBoostLabel() {
    DesignTheme(isDarkMode = true) {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            BoostLabel(
                label = "Spendings",
                value = "12",
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
