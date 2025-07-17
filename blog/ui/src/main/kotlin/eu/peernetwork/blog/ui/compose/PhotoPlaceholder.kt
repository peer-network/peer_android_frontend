package eu.peernetwork.blog.ui.compose

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.theme.PeerTheme

@Composable
fun PhotoPlaceholder(
    modifier: Modifier = Modifier,
    contentPaddingValues: PaddingValues,
    cell: Int = 3,
    size: Int = cell,
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(cell),
        modifier = modifier,
        contentPadding = contentPaddingValues,
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items(count = size) {
            Box(modifier = Modifier.aspectRatio(1f)
                .background(MaterialTheme.colorScheme.tertiaryContainer))
        }
    }
}

@Composable
fun PhotoPlaceholder(modifier: Modifier = Modifier) {
    Box(modifier = Modifier.fillMaxSize()) {
        PhotoPlaceholder(modifier, PaddingValues(16.dp))
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun PreviewPhotoPlaceholder() {
    PeerTheme {
        PhotoPlaceholder(modifier = Modifier.fillMaxSize().padding(4.dp))
    }
}
