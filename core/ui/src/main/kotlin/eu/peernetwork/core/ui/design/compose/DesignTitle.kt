package eu.peernetwork.core.ui.design.compose

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.extension.annotate
import eu.peernetwork.core.ui.theme.PeerTheme

data class DesignTitleTextStyle(
    val span: SpanStyle,
    val style: TextStyle,
    val descriptionStyle: TextStyle,
)

@Composable
fun DesignTitle(
    title: String,
    caption: String,
    description: String,
    modifier: Modifier = Modifier,
    maxLines: Int = Int.MAX_VALUE,
    maxContentLines: Int = Int.MAX_VALUE,
    spacer: @Composable () -> Unit = {},
    style: DesignTitleTextStyle? = null,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
) {
    val slug = if (caption.isEmpty()) {
        caption
    } else {
        "#${caption}"
    }
    val textStyle = style ?: DesignTitleTextStyle(
        span = SpanStyle(
            fontStyle = FontStyle.Italic,
            fontWeight = FontWeight.Normal,
            fontSize = MaterialTheme.typography.bodySmall.fontSize,
            color = MaterialTheme.colorScheme.tertiary
        ),
        style = MaterialTheme.typography.bodyMedium.copy(
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        ),
        descriptionStyle = MaterialTheme.typography.bodySmall.copy(
            color = MaterialTheme.colorScheme.tertiary
        )
    )
    Column(
        modifier = modifier,
        verticalArrangement = verticalArrangement,
        horizontalAlignment = horizontalAlignment
    ) {
        Text(
            text = "$title $slug".annotate(slug, textStyle.span),
            style = textStyle.style,
            overflow = TextOverflow.Ellipsis,
            maxLines = maxLines,
        )
        spacer()
        if (description.isNotEmpty()) {
            Text(
                text = description,
                maxLines = maxContentLines,
                overflow = TextOverflow.Ellipsis,
                style = textStyle.descriptionStyle,
                modifier = Modifier.padding(top = 6.dp)
            )
        }
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun PreviewDesignDetail() {
    PeerTheme {
        Column {
            DesignTitle(
                title = "John Doe",
                caption = "1675262",
                description = "Hello, John...",
            )
            Spacer(modifier = Modifier.height(12.dp))
            DesignTitle(
                title = "John Doe",
                caption = "",
                description = "Hello, John...",
            )
            Spacer(modifier = Modifier.height(12.dp))
            DesignTitle(
                title = "John Doe",
                caption = "1675262",
                description = "",
            )
        }
    }
}
