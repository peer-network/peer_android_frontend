package eu.peernetwork.app.ui.compose

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import eu.peernetwork.app.R
import eu.peernetwork.core.ui.theme.DesignTheme

@Composable
fun FeatureLabel(
    title: String,
    painter: Painter,
    modifier: Modifier = Modifier,
    description: String? = null,
    spacer: Dp = 12.dp
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(bottom = spacer)
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surfaceDim)
            .padding(
                vertical = 42.dp,
                horizontal = 16.dp
            ).then(modifier)
    ) {
        Image(
            painter = painter,
            contentDescription = title,
        )
        Spacer(modifier = Modifier.padding(top = 12.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center
        )
        description?.let {
            Spacer(modifier = Modifier.padding(top = 4.dp))
            Text(
                text = it,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.outline,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun FeatureLabelPreview() {
    DesignTheme {
        Column(modifier = Modifier.fillMaxSize()
            .padding(24.dp)) {
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                FeatureLabel(
                    "Post and engage more",
                    painterResource(R.drawable.bg_shop),
                    modifier = Modifier.weight(1f)
                )
                FeatureLabel(
                    title = "Post and engage more",
                    painter = painterResource(R.drawable.bg_ad),
                    description = "Coming soon...",
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}
