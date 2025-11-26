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
import eu.peernetwork.blog.ui.model.UiMedia
import eu.peernetwork.blog.ui.model.UiPost
import eu.peernetwork.blog.ui.model.UiPost.Type
import kotlinx.collections.immutable.ImmutableList

@Composable
fun PostItem(
    type: Type,
    pinnedBy: String?,
    model: UiPost.Detail,
    media: ImmutableList<UiMedia>,
    onPin: (() -> Unit)? = null,
    onMenu: () -> Unit,
    onClick: () -> Unit,
    engagement: @Composable () -> Unit,
    connection: @Composable RowScope.() -> Unit,
    content: @Composable (String) -> Unit
) {
    val updatedContent by rememberUpdatedState(content)
    if (type == Type.TEXT) {
        PostScaffold(
            model = model,
            pinnedBy = pinnedBy,
            onPin = onPin,
            onMenu = onMenu,
            onClick = onClick,
            engagement = engagement,
            connection = connection,
        )
    } else {
        PostMediaScaffold(
            model = model,
            pinnedBy = pinnedBy,
            onPin = onPin,
            onMenu = onMenu,
            onClick = onClick,
            connection = connection,
            engagement = engagement
        ) {
            Box(modifier = Modifier.fillMaxWidth()) {
                if (media.size == 1) {
                    val path by remember { derivedStateOf { media.first().path } }
                    updatedContent(path)
                } else {
                    val pagerState = rememberPagerState(initialPage = 0) { media.size }
                    PostPager(
                        pagerState,
                        media,
                    ) { updatedContent(it) }
                }
            }
        }
    }
}
