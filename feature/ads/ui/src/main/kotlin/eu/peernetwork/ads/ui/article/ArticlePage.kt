package eu.peernetwork.ads.ui.article

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.design.luna.DesignRichText
import eu.peernetwork.core.ui.theme.DesignTheme
import eu.peernetwork.feature.ads.ui.R

@Composable
fun ArticlePage(
    title: AnnotatedString,
    description: AnnotatedString,
    modifier: Modifier = Modifier,
    onClick: (DesignRichText, String) -> Unit,
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
            .padding(horizontal = 12.dp)
            .padding(top = 12.dp)
            .then(modifier)
    ) {
        Text(
            text = stringResource(R.string.article_configuration_label),
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
        ArticleSummery(
            title = title,
            description = description,
            modifier = Modifier.padding(vertical = 12.dp),
            onClick = onClick,
            content = content
        )
    }
}

@Preview
@Composable
fun PreviewArticlePage() {
    DesignTheme(isDarkMode = true) {
        ArticlePage(
            title = buildAnnotatedString { append("Title") },
            description = buildAnnotatedString { append("There’s something about hiking that resets everything. ") },
            onClick = { _, _ -> }
        ) {}
    }
}
