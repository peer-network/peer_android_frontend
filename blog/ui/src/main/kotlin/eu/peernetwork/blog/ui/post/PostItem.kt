package eu.peernetwork.blog.ui.post

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import eu.peernetwork.blog.ui.model.UiAsset
import eu.peernetwork.blog.ui.model.UiAuthor
import eu.peernetwork.blog.ui.model.UiMedia
import eu.peernetwork.blog.ui.model.UiPostDetail
import eu.peernetwork.blog.ui.model.UiPostType
import eu.peernetwork.blog.ui.model.UiStatus
import eu.peernetwork.core.ui.design.luna.DesignRichText
import eu.peernetwork.core.ui.extension.tap

@Composable
fun PostItem(
    type: UiPostType,
    pinnedBy: String?,
    model: UiPostDetail,
    asset: UiAsset,
    status: UiStatus,
    isAuthor: Boolean,
    author: UiAuthor,
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
    val configuration = LocalConfiguration.current
    val titleWidth = (configuration.screenWidthDp * .3).dp
    val updatedContent by rememberUpdatedState(content)
    val pagerState = rememberPagerState(initialPage = 0) { asset.media.size }
    val isAuthorVisible = rememberSaveable { mutableStateOf(author.isAccessible || isAuthor) }
    if (type == UiPostType.TEXT) {
        PostScaffold(
            model = model,
            isAuthor = isAuthor,
            onClick = onClick,
            engagement = engagement,
            toolbar = {
                PostToolbar(
                    slug = model.slug,
                    status = author.status,
                    pinnedBy = pinnedBy,
                    isAccessible = author.isAccessible,
                    isAuthor = isAuthor,
                    isVisible = isAuthorVisible,
                    username = model.username,
                    imageUrl = model.imageUrl,
                    color = MaterialTheme.colorScheme.surfaceContainerLow,
                    modifier = Modifier.padding(10.dp),
                    onAuthorClick = onAuthorClick,
                    onMenu = onMenu,
                    connection = connection
                )
            }
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
            onClick = onClick,
            engagement = engagement,
            toolbar = {
                PostToolbar(
                    slug = model.slug,
                    status = author.status,
                    pinnedBy = pinnedBy,
                    isVisible = isAuthorVisible,
                    isAccessible = author.isAccessible,
                    isAuthor = isAuthor,
                    username = model.username,
                    imageUrl = model.imageUrl,
                    color = MaterialTheme.colorScheme.surfaceContainerLow,
                    modifier = Modifier.padding(10.dp),
                    onAuthorClick = onAuthorClick,
                    onMenu = onMenu,
                    connection = connection
                )
            }
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
            onClick = onClick,
            isVisible = isVisible,
            onContentClick = onContentClick,
            engagement = engagement,
            label = {
                PostLabelMask(
                    text = author.username,
                    status = author.status,
                    isAccessible = author.isAccessible,
                    isVisible = isAuthorVisible,
                    modifier = Modifier.widthIn(max = titleWidth)
                        .tap(onAuthorClick),
                )
            },
            toolbar = {
                PostToolbar(
                    slug = model.slug,
                    status = author.status,
                    pinnedBy = pinnedBy,
                    isVisible = isAuthorVisible,
                    isAccessible = author.isAccessible,
                    isAuthor = isAuthor,
                    username = model.username,
                    imageUrl = model.imageUrl,
                    color = MaterialTheme.colorScheme.surfaceContainerLow,
                    modifier = Modifier.padding(10.dp),
                    onAuthorClick = onAuthorClick,
                    onMenu = onMenu,
                    connection = connection
                )
            }
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
