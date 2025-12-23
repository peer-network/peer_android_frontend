package eu.peernetwork.blog.ui.gallery

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import eu.peernetwork.blog.ui.model.UiAsset
import eu.peernetwork.blog.ui.model.UiMedia
import eu.peernetwork.blog.ui.post.PostPager

@Composable
fun GalleryPager(
    asset: UiAsset,
    content: @Composable BoxWithConstraintsScope.(UiMedia) -> Unit
) {
    val updatedContent by rememberUpdatedState(content)
    if (asset.media.size == 1) {
        val media by remember { derivedStateOf { asset.media.first() } }
        BoxWithConstraints(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) { updatedContent(this, media) }
    } else {
        val pagerState = rememberPagerState(initialPage = 0) { asset.media.size }
        PostPager(
            pagerState,
            asset,
        ) {
            BoxWithConstraints(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) { updatedContent(this, it) }
        }
    }
}
