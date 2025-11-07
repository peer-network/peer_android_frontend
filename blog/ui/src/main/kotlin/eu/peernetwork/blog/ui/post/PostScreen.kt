package eu.peernetwork.blog.ui.post

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelStoreOwner
import eu.peernetwork.blog.ui.engagement.EngagementScreen
import eu.peernetwork.blog.ui.model.UiMedia
import eu.peernetwork.blog.ui.model.UiPost
import eu.peernetwork.blog.ui.model.UiPost.Type
import eu.peernetwork.blog.ui.moderation.ModerationScreen
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.extension.builder
import kotlinx.collections.immutable.ImmutableList

@Composable
fun PostScreen(
    id: String,
    limit: Int,
    listState: LazyListState = rememberLazyListState(),
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    connection: @Composable RowScope.(Triple<String, Boolean, Boolean>) -> Unit,
    content: @Composable (Post.Component, PostEvent) -> Unit
) {
    val context = LocalContext.current
    val component = remember {
        provider.builder(Post.Builder::class.java).build(context)
    }
    val current = remember { mutableIntStateOf(-1) }
    val updatedContent by rememberUpdatedState(content)
    EngagementScreen(
        userId = id,
        postLimit = limit,
        onAuthorClick = {  },
        onMentionClick = {  },
        onHashtagClick = {  },
        provider = component,
        viewModelStoreOwner = viewModelStoreOwner,
        connection = connection
    ) { engagement ->
        val event = remember { object : PostEvent {} }
        ModerationScreen(
            provider = component,
            viewModelStoreOwner = viewModelStoreOwner
        ) { moderation ->
            PostList(
                listState = listState,
                onFocused = { current.intValue = it },
                onFocus = { position -> }
            ) { updatedContent(component, event) }
        }
    }
}

@Composable
fun PostScreen(
    type: Type,
    pinnedBy: String?,
    model: UiPost.Detail,
    media: ImmutableList<UiMedia>,
    engagement: UiPost.Engagement,
    connection: @Composable () -> Unit,
    image: @Composable (String) -> Unit,
    video: @Composable () -> Unit,
    content: @Composable () -> Unit
) {
    val updatedImage by rememberUpdatedState(image)
    val updatedVideo by rememberUpdatedState(video)
    if (type == Type.TEXT) {
        PostScaffold(
            model = model,
            pinnedBy = pinnedBy,
            engagement = engagement,
            connection = connection,
        )
    } else if (type == Type.IMAGE) {
        PostScaffold(
            model = model,
            engagement = engagement,
            pinnedBy = pinnedBy,
            connection = connection
        ) {
            Box(modifier = Modifier.fillMaxWidth()) {
                if (media.size == 1) {
                    val path by remember { derivedStateOf { media.first().path } }
                    updatedImage(path)
                } else {
                    val pagerState = rememberPagerState(initialPage = 0) { media.size }
                    PostPager(
                        pagerState,
                        media,
                    ) { updatedImage(it) }
                }
            }
        }
    }
}
