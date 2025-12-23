package eu.peernetwork.blog.ui.gallery

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.design.luna.DesignRichText
import eu.peernetwork.core.ui.theme.DesignTheme

@Composable
fun GalleryCaption(
    description: AnnotatedString,
    modifier: Modifier = Modifier,
    onClick: (DesignRichText, String) -> Unit,
) {
    val textLength = description.text.length
    val style = when {
        textLength < 50 -> MaterialTheme.typography.titleLarge
        textLength < 150 -> MaterialTheme.typography.titleMedium
        textLength < 300 -> MaterialTheme.typography.bodyLarge
        textLength < 500 -> MaterialTheme.typography.bodyMedium
        else -> MaterialTheme.typography.bodySmall
    }
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState())
            .then(modifier)
    ) {
        DesignRichText(
            text = description,
            style = style,
            fontWeight = FontWeight.Normal,
            color = MaterialTheme.colorScheme.onBackground,
            onClick = onClick
        )
    }
}

@Composable
@Preview
fun PreviewGalleryCaptionShort() {
    DesignTheme(isDarkMode = true) {
        GalleryCaption(
            description = AnnotatedString("Short text"),
            onClick = { _, _ -> }
        )
    }
}

@Composable
@Preview
fun PreviewGalleryCaptionMedium() {
    DesignTheme(isDarkMode = true) {
        GalleryCaption(
            description = AnnotatedString("This is a sample caption for the gallery item. It may be long or short, depending on the content."),
            onClick = { _, _ -> }
        )
    }
}

@Composable
@Preview
fun PreviewGalleryCaptionLong() {
    DesignTheme(isDarkMode = true) {
        GalleryCaption(
            description = AnnotatedString("This is a much longer sample caption for the gallery item. It contains significantly more text to demonstrate how the component adjusts its text size based on content length. The longer the text, the smaller the font size will become to ensure everything fits nicely within the available space. This helps maintain good readability while accommodating varying content lengths. Here's even more text to make it really long and test the smallest font size."),
            onClick = { _, _ -> }
        )
    }
}