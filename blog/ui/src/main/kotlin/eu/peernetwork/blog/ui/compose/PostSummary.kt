package eu.peernetwork.blog.ui.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import eu.peernetwork.blog.ui.mapper.annotateTag

@Composable
fun PostSummary(
    username: String,
    title: String,
    description: String,
    color: Color = MaterialTheme.colorScheme.onBackground,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier) {
        Text(
            text = username,
            style = MaterialTheme.typography.headlineMedium.copy(
                color = color,
                fontStyle = FontStyle.Italic
            ),
            modifier = Modifier.padding(end = 8.dp)
        )
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium.copy(color = color),
            )
            if (description.isNotEmpty()) {
                Text(
                    text = description.annotateTag(
                        MaterialTheme.typography.bodySmall.toSpanStyle().copy(
                            color = MaterialTheme.colorScheme.primary
                        )
                    ),
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.tertiary,
                    ),
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
        }
    }
}
