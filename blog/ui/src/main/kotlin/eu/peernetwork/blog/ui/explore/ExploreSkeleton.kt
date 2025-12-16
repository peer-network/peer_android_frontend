package eu.peernetwork.blog.ui.explore

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.design.luna.DesignSkeleton
import eu.peernetwork.core.ui.design.material.DesignShimmer
import eu.peernetwork.core.ui.theme.DesignTheme

@Composable
fun ExploreSkeleton(count: Int) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(count),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(4.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items(count) {
            ExploreSkeleton()
        }
    }
}

@Composable
fun ExploreSkeleton() {
    DesignShimmer {
        DesignSkeleton(
            shape = RectangleShape,
            modifier = Modifier.aspectRatio(1f)
        )
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun PreviewExploreSkeleton() {
    DesignTheme {
        ExploreSkeleton(3)
    }
}
