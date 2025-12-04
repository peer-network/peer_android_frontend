package eu.peernetwork.blog.ui.post

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import eu.peernetwork.blog.ui.model.v2.UiPostDetail
import eu.peernetwork.core.ui.design.luna.DesignRichText

@Composable
fun PostText(
    model: UiPostDetail,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(
        start = 12.dp,
        end = 12.dp,
        bottom = 16.dp,
    ),
) {
    Column(modifier = Modifier.then(modifier)
        .padding(contentPadding)) {
        DesignRichText(
            text = model.title,
            maxLines = 2,
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.bodyMedium,
        )
        DesignRichText(
            text = model.description,
            maxLines = 6,
            lineHeight = 18.sp,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.outline,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}
