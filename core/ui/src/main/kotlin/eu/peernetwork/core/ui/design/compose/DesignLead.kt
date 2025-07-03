package eu.peernetwork.core.ui.design.compose

import android.content.res.Configuration
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.R
import eu.peernetwork.core.ui.extension.annotate
import eu.peernetwork.core.ui.theme.PeerTheme

data class DesignTitleStyle(
    val span: SpanStyle,
    val style: androidx.compose.ui.text.TextStyle,
    val descriptionStyle: androidx.compose.ui.text.TextStyle,
)

@Composable
fun DesignLead(
    title: String,
    caption: String,
    description: String,
    modifier: Modifier = Modifier,
    maxLines: Int = Int.MAX_VALUE,
    maxContentLines: Int = 3,
    spacer: @Composable () -> Unit = {},
    style: DesignTitleStyle? = null,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
) {
    val slug = if (caption.isEmpty()) {
        caption
    } else {
        "#${caption}"
    }
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

    var isExpanded by remember { mutableStateOf(false) }
    var readyToDraw by remember { mutableStateOf(false) }
    var cutDescription by remember { mutableStateOf(description) }
    var showMoreNeeded by remember { mutableStateOf(false) }

    val showMoreText = stringResource(id = R.string.show_more_text)
    val showLessText = stringResource(id = R.string.show_less_text)

    val linkColor = MaterialTheme.colorScheme.tertiary
    val linkFontWeight = FontWeight.Bold


    val annotatedDescription = remember(isExpanded, cutDescription, showMoreNeeded) {
        if (isExpanded) {
            buildAnnotatedString {
                append(cutDescription)
                append(" ")
                pushStringAnnotation(tag = "SHOW_LESS", annotation = "show_less")
                withStyle(
                    style = SpanStyle(color = linkColor, fontWeight = linkFontWeight)
                ) {
                    append(showLessText)
                }
                pop()
            }
        } else {
            buildAnnotatedString {
                append(cutDescription)
                if (showMoreNeeded) {
                    pushStringAnnotation(tag = "SHOW_MORE", annotation = "show_more")
                    withStyle(
                        style = SpanStyle(color = linkColor, fontWeight = linkFontWeight)
                    ) {
                        append(showMoreText)
                    }
                    pop()
                }
            }
        }
    }

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
        if (description.isNotEmpty()) {
            ClickableText(
                text = annotatedDescription,
                maxLines = if (isExpanded) Int.MAX_VALUE else maxContentLines,
                overflow = TextOverflow.Ellipsis,
                style = textStyle.descriptionStyle,
                modifier = Modifier
                    .padding(
                        top = 2.dp,
                        end = 4.dp
                    )
                    .animateContentSize(),
                onTextLayout = { layoutResult ->
                    if (!isExpanded) {
                        if (!readyToDraw) {
                            if (layoutResult.hasVisualOverflow) {
                                val lastVisibleCharIndex = layoutResult.getLineEnd(
                                    maxContentLines - 1,
                                    visibleEnd = true
                                )
                                val cutoffIndex = (lastVisibleCharIndex - showMoreText.length).coerceAtLeast(0)
                                cutDescription = description.substring(0, cutoffIndex)
                                showMoreNeeded = true
                            } else {
                                cutDescription = description
                                showMoreNeeded = false
                            }
                            readyToDraw = true
                        }
                    } else {

                        cutDescription = description
                        showMoreNeeded = false
                        readyToDraw = false
                    }
                },
                onClick = { offset ->
                    annotatedDescription.getStringAnnotations(tag = "SHOW_MORE", start = offset, end = offset)
                        .firstOrNull()?.let {
                            isExpanded = true
                            readyToDraw = false
                            return@ClickableText
                        }
                    annotatedDescription.getStringAnnotations(tag = "SHOW_LESS", start = offset, end = offset)
                        .firstOrNull()?.let {
                            isExpanded = false
                            readyToDraw = false
                            return@ClickableText
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
                description = "Hello, John, this is a longer description that will demonstrate how the more less expandable text works in the DesignLead component.",
                maxContentLines = 3
            )
            Spacer(modifier = Modifier.height(12.dp))
            DesignLead(
                title = "John Doe",
                caption = "",
                description = "Hello, John, this is a longer description that will demonstrate how the more less expandable text works in the DesignLead component.",
                maxContentLines = 3
            )
            Spacer(modifier = Modifier.height(12.dp))
            DesignLead(
                title = "John Doe",
                caption = "1675262",
                description = "",
            )
        }
    }
}
