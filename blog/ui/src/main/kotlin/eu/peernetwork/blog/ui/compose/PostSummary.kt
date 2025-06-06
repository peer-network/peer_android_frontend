package eu.peernetwork.blog.ui.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.design.compose.DesignRichText
import eu.peernetwork.core.ui.design.compose.DesignTitleStyle

@Composable
fun PostSummary(
    username: String,
    title: AnnotatedString,
    description: AnnotatedString,
    userOnClick: () -> Unit = {},
    onMentionClick: (String) -> Unit = {},
    onHashtagClick: (String) -> Unit = {},
    color: Color = MaterialTheme.colorScheme.onBackground,
    modifier: Modifier = Modifier
) {
    val handleUserClick by rememberUpdatedState(userOnClick)
    Row(modifier = modifier) {
        Text(
            text = username,
            style = MaterialTheme.typography.headlineMedium.copy(
                color = color,
                fontStyle = FontStyle.Italic
            ),
            modifier = Modifier
                .padding(end = 8.dp)
                .clickable { handleUserClick() }
        )
        DesignRichText(
            title = title,
            description = description,
            style = DesignTitleStyle(
                style = MaterialTheme.typography.bodyMedium.copy(color = color),
                descriptionStyle = MaterialTheme.typography.bodySmall.copy(
                    color = MaterialTheme.colorScheme.tertiary
                ),
                span = SpanStyle(
                    fontStyle = FontStyle.Italic,
                    fontWeight = FontWeight.Normal,
                    fontSize = MaterialTheme.typography.bodySmall.fontSize,
                    color = MaterialTheme.colorScheme.tertiary
                ),
            ),
            onMentionClick = onMentionClick,
            onHashtagClick = onHashtagClick
        )
    }
}
