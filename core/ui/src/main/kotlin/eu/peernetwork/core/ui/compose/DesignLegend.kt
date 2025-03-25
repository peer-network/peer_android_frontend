package eu.peernetwork.core.ui.compose

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import eu.peernetwork.core.ui.theme.PeerTheme

@Composable
fun DesignLegend(
    title: @Composable () -> Unit,
    subTitle: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = MaterialTheme.typography.bodyMedium,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    horizontalAlignment: Alignment.Horizontal = Alignment.CenterHorizontally,
) {
    Column(
        modifier = modifier,
        verticalArrangement = verticalArrangement,
        horizontalAlignment = horizontalAlignment
    ) {
        CompositionLocalProvider(LocalTextStyle provides textStyle.copy(
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
        )) { title() }
        CompositionLocalProvider(LocalTextStyle provides textStyle.copy(
            color = MaterialTheme.colorScheme.tertiary,
            fontSize = MaterialTheme.typography.bodySmall.fontSize
        )) { subTitle() }
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun PreviewDesignLegend() {
    PeerTheme {
        DesignLegend(
            title = { Text(text = "432") },
            subTitle = { Text(text = "publications") }
        )
    }
}
