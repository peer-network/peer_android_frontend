package eu.peernetwork.social.ui.search.title

import android.content.res.Configuration
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.design.luna.DesignSkeleton
import eu.peernetwork.core.ui.design.material.DesignShimmer
import eu.peernetwork.core.ui.theme.DesignTheme

@Composable
fun TitleSkeleton(count: Int) {
    LazyColumn {
        items(count) {
            TitleSkeleton(
                Modifier.fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .padding(horizontal = 16.dp)
            )
        }
    }
}

@Composable
fun TitleSkeleton(modifier: Modifier) {
    DesignShimmer {
        Row(
            modifier = modifier,
            verticalAlignment = Alignment.CenterVertically
        ) {
            DesignSkeleton(
                color = MaterialTheme.colorScheme.surfaceContainerLowest,
                modifier = Modifier.size(36.dp)
            )
            DesignSkeleton(
                color = MaterialTheme.colorScheme.surfaceContainerLowest,
                modifier = Modifier.padding(start = 10.dp)
                    .height(16.dp)
                    .fillMaxWidth(fraction = .3f)
            )
        }
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun PreviewTitleSkeleton() {
    DesignTheme(isDarkMode = true) {
        TitleSkeleton(3)
    }
}
