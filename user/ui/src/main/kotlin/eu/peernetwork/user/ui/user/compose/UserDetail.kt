package eu.peernetwork.user.ui.user.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.extension.annotate
import eu.peernetwork.user.ui.R

@Composable
fun UserDetail(
    slug: Int,
    username: String,
    bio: String?,
) {
    Column {
        Text(
            text = "$username #$slug".annotate(
                "#$slug",
                SpanStyle(
                    fontStyle = FontStyle.Italic,
                    fontWeight = FontWeight.Normal,
                    fontSize = MaterialTheme.typography.bodySmall.fontSize,
                    color = MaterialTheme.colorScheme.tertiary
                )
            ),
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            ),
        )
        Text(
            text = bio ?: stringResource(R.string.empty_description_message),
            style = MaterialTheme.typography.bodySmall.copy(
                color = MaterialTheme.colorScheme.tertiary
            ),
            modifier = Modifier.padding(top = 6.dp)
        )
    }
}
