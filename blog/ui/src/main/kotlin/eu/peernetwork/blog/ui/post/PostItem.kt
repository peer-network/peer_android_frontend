package eu.peernetwork.blog.ui.post

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import eu.peernetwork.blog.ui.model.v2.UiAsset
import eu.peernetwork.blog.ui.model.v2.UiPostDetail
import eu.peernetwork.blog.ui.model.v2.UiPostType

@Composable
fun PostItem(
    type: UiPostType,
    pinnedBy: String?,
    model: UiPostDetail,
    asset: UiAsset,
    onMenu: () -> Unit,
    onClick: () -> Unit,
    engagement: @Composable () -> Unit,
    connection: @Composable RowScope.() -> Unit,
    content: @Composable (String, Boolean) -> Unit
) {
    val updatedContent by rememberUpdatedState(content)
    val pagerState = rememberPagerState(initialPage = 0) { asset.media.size }
    if (type == UiPostType.TEXT) {
        PostScaffold(
            model = model,
            pinnedBy = pinnedBy,
            onMenu = onMenu,
            onClick = onClick,
            engagement = engagement,
            connection = connection,
        )
    } else if (type == UiPostType.AUDIO && asset.media.any { it.display.cover == null }) {
        PostScaffold(
            model = model,
            pinnedBy = pinnedBy,
            onMenu = onMenu,
            onClick = onClick,
            engagement = engagement,
            connection = connection,
        ) {
            PostText(
                model = model,
                contentPadding = PaddingValues(horizontal = 12.dp)
            )
            Box(modifier = Modifier.fillMaxWidth()) {
                updatedContent(asset.media.first().path, false)
            }
        }
    } else {
        PostExpandedScaffold(
            model = model,
            pinnedBy = pinnedBy,
            onMenu = onMenu,
            onClick = onClick,
            connection = connection,
            engagement = engagement
        ) {
            Box(modifier = Modifier.fillMaxWidth()) {
                if (asset.media.size == 1) {
                    updatedContent(asset.media.first().path, true)
                } else {
                    PostPager(
                        pagerState,
                        asset,
                    ) { updatedContent(it, true) }
                }
            }
        }
    }
}
