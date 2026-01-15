package eu.peernetwork.blog.ui.post

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import eu.peernetwork.blog.ui.model.UiAsset
import eu.peernetwork.blog.ui.model.UiMedia
import eu.peernetwork.blog.ui.model.UiPostDetail
import eu.peernetwork.blog.ui.model.UiPostType
import eu.peernetwork.blog.ui.model.UiStatus
import eu.peernetwork.core.ui.design.luna.DesignRichText

@Composable
fun PostItem(
    type: UiPostType,
    pinnedBy: String?,
    model: UiPostDetail,
    asset: UiAsset,
    status: UiStatus,
    isAuthor: Boolean,
    isAccessible: Boolean,
    isVisible: MutableState<Boolean>,
    onMenu: () -> Unit,
    onClick: () -> Unit,
    onAuthorClick: () -> Unit,
    onContentClick: (DesignRichText, String) -> Unit,
    engagement: @Composable () -> Unit,
    connection: @Composable RowScope.() -> Unit,
    content: @Composable (UiMedia, Boolean) -> Unit
) {
    val updatedContent by rememberUpdatedState(content)
    val pagerState = rememberPagerState(initialPage = 0) { asset.media.size }
    if (type == UiPostType.TEXT) {
        PostScaffold(
            model = model,
            isAuthor = isAuthor,
            pinnedBy = pinnedBy,
            status = status,
            isAccessible = isAccessible,
            onMenu = onMenu,
            onClick = onClick,
            onAuthorClick = onAuthorClick,
            engagement = engagement,
            connection = connection,
        ) {
            PostTextMask(
                status = status,
                isAuthor = isAuthor,
                isVisible = isVisible,
                isAccessible = isAccessible
            ) {
                PostText(
                    model = model,
                    modifier = Modifier.heightIn(min = 36.dp),
                    onClick = onContentClick
                )
            }
        }
    } else if (type == UiPostType.AUDIO && !asset.hasCover) {
        PostScaffold(
            model = model,
            isAuthor = isAuthor,
            pinnedBy = pinnedBy,
            status = status,
            isAccessible = isAccessible,
            onMenu = onMenu,
            onClick = onClick,
            onAuthorClick = onAuthorClick,
            engagement = engagement,
            connection = connection,
        ) {
            PostTextMask(
                status = status,
                isAuthor = isAuthor,
                isVisible = isVisible,
                isAccessible = isAccessible
            ) {
                Column {
                    PostText(
                        model = model,
                        onClick = onContentClick,
                        contentPadding = PaddingValues(horizontal = 12.dp)
                    )
                    Box(modifier = Modifier.fillMaxWidth()) {
                        updatedContent(asset.media.first(), false)
                    }
                }
            }
        }
    } else {
        PostExpandedScaffold(
            model = model,
            isAuthor = isAuthor,
            pinnedBy = pinnedBy,
            status = status,
            isAccessible = isAccessible,
            onMenu = onMenu,
            onClick = onClick,
            isVisible = isVisible,
            onAuthorClick = onAuthorClick,
            onContentClick = onContentClick,
            connection = connection,
            engagement = engagement
        ) {
            Box(modifier = Modifier.fillMaxWidth()) {
                if (asset.media.size == 1) {
                    updatedContent(asset.media.first(), true)
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
