package eu.peernetwork.core.ui.design.material

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.theme.PeerTheme

@Composable
fun DesignDetailLayout(
    modifier: Modifier = Modifier,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    verticalAlignment: Alignment.Vertical = Alignment.Top,
    lead: @Composable () -> Unit,
    content: @Composable ColumnScope.() -> Unit,
) {
    val updatedLead by rememberUpdatedState(lead)
    val updatedContent by rememberUpdatedState(content)
    Row(
        modifier = modifier,
        horizontalArrangement = horizontalArrangement,
        verticalAlignment = verticalAlignment
    ) {
        updatedLead()
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = verticalArrangement,
            horizontalAlignment = horizontalAlignment
        ) { updatedContent() }
    }
}

@Composable
fun DesignDetail(
    modifier: Modifier = Modifier,
    textStyle: TextStyle = MaterialTheme.typography.bodyMedium,
    lead: @Composable (() -> Unit)? = null,
    trailing: @Composable (() -> Unit)? = null,
    options: @Composable (() -> Unit)? = null,
    verticalAlignment: Alignment.Vertical = Alignment.Top,
    content: @Composable RowScope.() -> Unit,
) {
    val updatedLead by rememberUpdatedState(lead)
    val updatedTrailing by rememberUpdatedState(trailing)
    val updatedOptions by rememberUpdatedState(options)
    val updatedContent by rememberUpdatedState(content)
    DesignDetailLayout(
        modifier = modifier,
        lead = { updatedLead?.invoke() },
        verticalAlignment = verticalAlignment
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            CompositionLocalProvider(LocalTextStyle provides textStyle.copy(
                color = MaterialTheme.colorScheme.onBackground
            )) { updatedContent() }
            CompositionLocalProvider(
                LocalContentColor provides MaterialTheme.colorScheme.onSurface
            ) { updatedTrailing?.invoke() }
        }
        updatedOptions?.invoke()
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun PreviewDesignLayout() {
    PeerTheme {
        Column {
            DesignDetailLayout(
                lead = {
                    DesignAvatar{ Box(modifier = Modifier.size(64.dp)
                        .background(MaterialTheme.colorScheme.background))
                    } },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Content", modifier = Modifier.padding(start = 16.dp))
            }
            Spacer(modifier = Modifier.height(16.dp))
            DesignDetail(
                lead = { Text("Lead") },
                trailing = { Text("Trailing") },
                options = { Text("Options", modifier = Modifier.fillMaxWidth()) },
            ) {
                Text("Content", modifier = Modifier.weight(1f))
            }
        }
    }
}
