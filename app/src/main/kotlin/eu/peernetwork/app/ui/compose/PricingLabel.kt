package eu.peernetwork.app.ui.compose

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import eu.peernetwork.blog.ui.R
import eu.peernetwork.core.ui.theme.DesignTheme

@Composable
fun PricingLabel(
    lead: Painter,
    trailing: Painter,
    size: Dp = 20.dp,
    trailingSize: Dp = size,
    color: Color = Color.Unspecified,
    contentDescription: String? = null,
    content: @Composable BoxScope.() -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
            .clip(RoundedCornerShape(32.dp))
            .background(MaterialTheme.colorScheme.surfaceDim)
            .padding(12.dp)
            .padding(vertical = 2.dp)
            .padding(start = 4.dp)
    ) {
        Icon(
            painter = lead,
            contentDescription = contentDescription,
            modifier = Modifier.size(size),
            tint = color
        )
        Box(modifier = Modifier.weight(1f), content = content)
        Icon(
            painter = trailing,
            contentDescription = contentDescription,
            modifier = Modifier.size(trailingSize),
            tint = color
        )
    }
}

@Composable
fun PricingLabel(
    lead: Painter,
    trailing: Painter,
    size: Dp = 20.dp,
    trailingSize: Dp = size,
    color: Color = Color.Unspecified,
    contentDescription: String? = null,
    price: String,
    label: String
) {
    PricingLabel(
        lead = lead,
        trailing = trailing,
        size = size,
        trailingSize = trailingSize,
        color = color,
        contentDescription = contentDescription
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
                .padding(horizontal = 8.dp),
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = price,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun PricingLabelPreview() {
    DesignTheme {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            PricingLabel(
                lead = painterResource(R.drawable.ic_like),
                trailing = painterResource(R.drawable.ic_gem),
                price = "+ 2",
                label = "Got a like"
            )
            PricingLabel(
                lead = painterResource(R.drawable.ic_like),
                trailing = painterResource(R.drawable.ic_gem),
                price = "+ 2",
                label = "Got a like"
            )
        }
    }
}
