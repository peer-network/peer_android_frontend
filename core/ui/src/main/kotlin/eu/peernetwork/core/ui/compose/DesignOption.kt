package eu.peernetwork.core.ui.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp

enum class DesignOptionPosition {
    START,
    BOTTOM
}

@Composable
fun DesignOption(
    text: String,
    painter: Painter,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    contentPaddingValues: PaddingValues = PaddingValues(horizontal = 6.dp),
    tint: Color = LocalContentColor.current,
    textStyle: TextStyle = MaterialTheme.typography.bodySmall,
    position: DesignOptionPosition = DesignOptionPosition.START
) {
    when (position) {
        DesignOptionPosition.START -> {
            Row(
                modifier = modifier
                    .clickable(role = Role.Button, onClick = onClick)
                    .padding(contentPaddingValues),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painter,
                    tint = tint,
                    contentDescription = contentDescription,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(text, style = textStyle)
            }
        }
        DesignOptionPosition.BOTTOM -> {
            Column(
                modifier = modifier
                    .clickable(role = Role.Button, onClick = onClick)
                    .padding(contentPaddingValues),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    painter = painter,
                    tint = tint,
                    contentDescription = contentDescription,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(text, style = textStyle)
            }
        }
    }
}
