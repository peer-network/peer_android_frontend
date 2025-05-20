package eu.peernetwork.app.ui.renderer

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import eu.peernetwork.app.BuildConfig
import eu.peernetwork.blog.ui.post.photo.PhotoScreen
import eu.peernetwork.blog.ui.post.video.VideoScreen
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.compose.DesignTab
import eu.peernetwork.media.core.model.UiMimeType
import eu.peernetwork.social.ui.renderder.BlogRenderer

class BlogRendererDelegate(val provider: UiComponentProvider) : BlogRenderer {
    @Composable
    override fun invoke(
        modifier: Modifier,
        spec: BlogRenderer.Spec
    ) {
        when (spec.type) {
            BlogRenderer.Type.PHOTO -> PhotoScreen(
                spec.id,
                BuildConfig.PAGING_LIMIT,
                spec.state,
                provider,
                spec.viewModelStoreOwner,
                spec.onMentionClick,
                spec.onHashtagClick,
                spec.imageOnClick,
                spec.photoState
            )
            BlogRenderer.Type.VIDEO -> VideoScreen(
                spec.id,
                BuildConfig.PAGING_LIMIT,
                spec.state,
                provider,
                spec.viewModelStoreOwner,
                spec.onMentionClick,
                spec.onHashtagClick,
                spec.imageOnClick,
                spec.videoState
            )
            BlogRenderer.Type.UNSPECIFIED -> BlogScreen { offset ->
                when (offset) {
                    0 -> PhotoScreen(
                        spec.id,
                        BuildConfig.PAGING_LIMIT,
                        spec.state,
                        provider,
                        spec.viewModelStoreOwner,
                        spec.onMentionClick,
                        spec.onHashtagClick,
                        spec.imageOnClick,
                        spec.photoState
                    )
                    1 -> VideoScreen(
                        spec.id,
                        BuildConfig.PAGING_LIMIT,
                        spec.state,
                        provider,
                        spec.viewModelStoreOwner,
                        spec.onMentionClick,
                        spec.onHashtagClick,
                        spec.imageOnClick,
                        spec.videoState
                    )
                }
            }
        }
    }
}

@Composable
private fun BlogScreen(
    onNavigate: (Int) -> Unit = {},
    content: @Composable (Int) -> Unit
) {
    val pageState = rememberPagerState(
        pageCount = { UiMimeType.TYPES.size },
        initialPage = 0
    )
    DesignTab(pageState) { index ->
        UiMimeType.get(index)?.let {
            Icon(
                painter = painterResource(id = it.id),
                contentDescription = it.label?.let { stringResource(it) },
                tint = MaterialTheme.colorScheme.tertiary,
                modifier = Modifier.padding(vertical = 8.dp).size(28.dp)
            )
        }
    }
    HorizontalPager(
        state = pageState,
        verticalAlignment = Alignment.Top,
    ) { page -> content(page) }
    LaunchedEffect(pageState.currentPage) { onNavigate(pageState.currentPage) }
}
