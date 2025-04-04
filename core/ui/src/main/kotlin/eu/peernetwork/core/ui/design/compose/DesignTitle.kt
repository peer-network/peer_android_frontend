package eu.peernetwork.core.ui.design.compose

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
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
    title: State<String>,
    caption: State<String>,
    description: State<String>,
    modifier: Modifier = Modifier,
    spacer: (@Composable () -> Unit)? = null,
    style: DesignTitleTextStyle? = null,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
) {
    val slug = remember { derivedStateOf {
        if (caption.value.isEmpty()) {
            caption.value
        } else {
            "#${caption.value}"
        }
    } }
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
            text = "${title.value} ${slug.value}"
                .annotate(slug.value, textStyle.span),
            style = textStyle.style,
        )
        spacer ?: Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = description.value,
            style = textStyle.descriptionStyle
        )
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun PreviewDesignDetail() {
    PeerTheme {
        val title = remember { mutableStateOf("John Doe") }
        val caption = remember { mutableStateOf("1675262") }
        val description = remember { mutableStateOf("Hello, John...") }
        Column {
            DesignTitle(
                title = title,
                caption = remember { mutableStateOf("") },
                description = description,
            )
            Spacer(modifier = Modifier.height(12.dp))
            DesignTitle(
                title = title,
                caption = caption,
                description = description,
            )
        }
    }
}
