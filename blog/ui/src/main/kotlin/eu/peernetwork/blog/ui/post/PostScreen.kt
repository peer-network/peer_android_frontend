package eu.peernetwork.blog.ui.post

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelStoreOwner
import eu.peernetwork.blog.ui.engagement.EngagementObserver
import eu.peernetwork.blog.ui.engagement.v2.EngagementScreen
import eu.peernetwork.blog.ui.engagement.v2.EngagementOption
import eu.peernetwork.blog.ui.model.UiMedia
import eu.peernetwork.blog.ui.model.UiPost
import eu.peernetwork.blog.ui.model.UiPost.Type
import eu.peernetwork.blog.ui.model.UiReaction
import eu.peernetwork.blog.ui.moderation.v2.ModerationScreen
import eu.peernetwork.blog.ui.moderation.v2.ModerationScreenEvent
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.extension.builder
import kotlinx.collections.immutable.ImmutableList

@Composable
fun PostScreen(
    id: String,
    limit: Int,
    listState: LazyListState,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    connection: @Composable RowScope.(Triple<String, Boolean, Boolean>) -> Unit,
    content: @Composable (Post.Component, EngagementOption, ModerationScreenEvent, State<Int>) -> Unit
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
        val event = remember { object : EngagementOption {
            override fun observe(): State<Map<String, UiReaction>> = engagement.observe()
            override fun invoke(post: UiPost, state: EngagementOption.State) {
                when (state) {
                    EngagementOption.State.Like -> engagement(
                        EngagementObserver.State.Like(
                            id = post.id,
                            author = post.author.id,
                            message = post.title.text
                    ))
                    EngagementOption.State.Dislike -> engagement(
                        EngagementObserver.State.Dislike(post.id))
                    EngagementOption.State.Comment ->engagement(
                        EngagementObserver.State.Comment(post.id))
                    EngagementOption.State.View -> {
                        engagement(EngagementObserver.State.View(post.id))
                    }
                }
            }
        } }
        ModerationScreen(
            provider = component,
            viewModelStoreOwner = viewModelStoreOwner
        ) { moderation ->
            PostList(
                listState = listState,
                onFocused = { current.intValue = it },
                onFocus = { position -> }
            ) { updatedContent(component, event, moderation, it) }
        }
    }
}

@Composable
fun PostScreen(
    type: Type,
    pinnedBy: String?,
    model: UiPost.Detail,
    media: ImmutableList<UiMedia>,
    onPin: (() -> Unit)? = null,
    onMenu: () -> Unit,
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
            engagement = engagement,
            connection = connection,
        )
    } else {
        PostMediaScaffold(
            model = model,
            pinnedBy = pinnedBy,
            onPin = onPin,
            onMenu = onMenu,
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
