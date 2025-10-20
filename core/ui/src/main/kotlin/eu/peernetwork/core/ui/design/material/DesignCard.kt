package eu.peernetwork.core.ui.design.material

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.theme.PeerTheme

@Composable
fun DesignCard(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.surfaceBright,
    shape: Shape = RoundedCornerShape(16.dp),
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
    contentPadding: PaddingValues = PaddingValues(16.dp),
    leading: @Composable (() -> Unit)? = null,
    trailing: @Composable (() -> Unit)? = null,
    content: @Composable (BoxScope.() -> Unit),
) {
    val updatedLeading by rememberUpdatedState(leading)
    val updatedTrailing by rememberUpdatedState(trailing)
    val updatedContent by rememberUpdatedState(content)
    Row(
        horizontalArrangement = horizontalArrangement,
        verticalAlignment = verticalAlignment,
        modifier = modifier
        .clip(shape)
        .background(color)
        .padding(contentPadding)
    ) {
        Box(modifier = Modifier.wrapContentWidth()) { updatedLeading?.invoke() }
        Box(modifier = Modifier.weight(1f)) { updatedContent() }
        Box(modifier = Modifier.wrapContentWidth()) { updatedTrailing?.invoke() }
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun PreviewDesignCard() {
    PeerTheme {
        DesignCard(
            modifier = Modifier.background(MaterialTheme.colorScheme.background),
            leading = { Text("leading") },
            trailing = { Text("trailing") }
        ) { Text( text = "Hello, world!") }
    }
}
