package eu.peernetwork.core.ui.design.compose

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import eu.peernetwork.core.ui.extension.annotate
import eu.peernetwork.core.ui.theme.PeerTheme

data class DesignTitleStyle(
    val span: SpanStyle,
    val style: TextStyle,
    val descriptionStyle: TextStyle,
)

@Composable
fun DesignLead(
    title: String,
    caption: String,
    description: AnnotatedString,
    modifier: Modifier = Modifier,
    maxLines: Int = Int.MAX_VALUE,
    spacer: @Composable () -> Unit = {},
    style: DesignTitleStyle? = null,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    onMentionClick: (String) -> Unit = {},
    onHashtagClick: (String) -> Unit = {}
) {
    val slug = if (caption.isEmpty()) {
        caption
    } else {
        "#${caption}"
    }
    val uriHandler = LocalUriHandler.current
    val handleMention by rememberUpdatedState(onMentionClick)
    val handleHashTag by rememberUpdatedState(onHashtagClick)

    val updateSpacer by rememberUpdatedState(spacer)
    val textStyle = style ?: DesignTitleStyle(
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
        updateSpacer()
        if (description.text.isNotEmpty()) {
            DesignTextExpandable(
                text = description,
                maxLinesWhenCollapsed = 3,
                style = MaterialTheme.typography.labelLarge.copy(
                    color = MaterialTheme.colorScheme.tertiary,
                    lineHeight = 18.sp
                ),
                modifier = Modifier
                    .padding(top = 4.dp),
                content = { text, maxLines, modifier, style, onClick ->
                    ClickableText(
                        text = text,
                        maxLines = maxLines,
                        modifier = modifier,
                        style = style,
                        onClick = onClick ?: {}
                    )
                },
                onAnnotationClick = { tag, item ->
                    when (tag) {
                        "URL" -> uriHandler.openUri(item.lowercase())
                        "MENTION" -> handleMention(item)
                        "HASHTAG" -> handleHashTag(item)
                    }
                }
            )
        }
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun PreviewDesignTitle() {
    PeerTheme {
        Column {
            DesignLead(
                title = "John Doe",
                caption = "1675262",
                description = AnnotatedString("Hello, John..."),
            )
            Spacer(modifier = Modifier.height(12.dp))
            DesignLead(
                title = "John Doe",
                caption = "",
                description = AnnotatedString("Hello, John..."),
            )
            Spacer(modifier = Modifier.height(12.dp))
            DesignLead(
                title = "John Doe",
                caption = "1675262",
                description = AnnotatedString(""),
            )
        }
    }
}
