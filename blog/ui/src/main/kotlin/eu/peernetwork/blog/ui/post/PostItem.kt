package eu.peernetwork.blog.ui.post

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
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
    content: @Composable (String) -> Unit
) {
    val updatedContent by rememberUpdatedState(content)
    if (type == UiPostType.TEXT) {
        PostScaffold(
            model = model,
            pinnedBy = pinnedBy,
            onMenu = onMenu,
            onClick = onClick,
            engagement = engagement,
            connection = connection,
        )
    } else {
        PostMediaScaffold(
            model = model,
            pinnedBy = pinnedBy,
            onMenu = onMenu,
            onClick = onClick,
            connection = connection,
            engagement = engagement
        ) {
            Box(modifier = Modifier.fillMaxWidth()) {
                if (asset.media.size == 1) {
                    val path by remember { derivedStateOf { asset.media.first().path } }
                    updatedContent(path)
                } else {
                    val pagerState = rememberPagerState(initialPage = 0) { asset.media.size }
                    PostPager(
                        pagerState,
                        asset,
                    ) { updatedContent(it) }
                }
            }
        }
    }
}
