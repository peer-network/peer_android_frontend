package eu.peernetwork.social.ui.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.design.compose.DesignAsyncImage
import eu.peernetwork.core.ui.extension.annotate
import eu.peernetwork.social.ui.model.UiMember

@Composable
fun MemberItem(
    model: UiMember,
    onClick: (String) -> Unit = {}
) {
    val slug = "#${model.slug}"

    Row(
        modifier = Modifier
            .clickable { onClick(model.id) }
            .padding(vertical = 8.dp)
    ) {
        DesignAsyncImage(
            label = model.username,
            imageUrl = model.imageUrl ?: "http://localhost",
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = "@${model.username}$slug".annotate(slug, style = MaterialTheme.typography.bodySmall.toSpanStyle().copy(
                color = MaterialTheme.colorScheme.tertiary
            )),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(top = 12.dp)
        )
    }
}
