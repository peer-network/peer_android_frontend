package eu.peernetwork.blog.ui.gallery

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import eu.peernetwork.core.ui.design.luna.DesignRichText
import eu.peernetwork.core.ui.theme.DesignTheme

@Composable
fun GalleryDetail(
    title: AnnotatedString,
    description: AnnotatedString,
    time: String,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(12.dp),
    onClick: (DesignRichText, String) -> Unit,
) {
    Column(modifier = Modifier.then(modifier)
        .padding(contentPadding)) {
        DesignRichText(
            text = title,
            maxLines = 1,
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.bodyMedium,
            onClick = onClick
        )
        if (description.trim().isNotEmpty()) {
            DesignRichText(
                text = description,
                maxLines = 2,
                lineHeight = 18.sp,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.outline,
                modifier = Modifier.padding(top = 4.dp),
                onClick = onClick
            )
        }
        Text(
            text = time,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.outline,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

@Composable
@Preview
fun PreviewGalleryDetail() {
    DesignTheme(isDarkMode = true) {
        GalleryDetail(
            time = "2hr ago",
            title = buildAnnotatedString { append("John Doe") },
            description = buildAnnotatedString {
                append("This is a mock description for a content post. It's purely for testing.")
            },
        ) { _,_ -> }
    }
}
