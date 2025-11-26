package eu.peernetwork.blog.ui.post

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.pager.PagerScope
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import eu.peernetwork.blog.ui.engagement.EngagementObserver.State as ObserverState
import eu.peernetwork.blog.ui.engagement.v2.EngagementScreen
import eu.peernetwork.blog.ui.engagement.v2.EngagementOption
import eu.peernetwork.blog.ui.engagement.v2.EngagementOption.State as EngagementState
import eu.peernetwork.blog.ui.model.UiPost
import eu.peernetwork.blog.ui.model.UiReaction
import eu.peernetwork.blog.ui.moderation.v2.ModerationScreen
import eu.peernetwork.blog.ui.moderation.v2.ModerationScreenEvent
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.extension.builder

@Composable
fun PostScreen(
    id: String,
    limit: Int,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    connection: @Composable RowScope.(Triple<String, Boolean, Boolean>) -> Unit,
    content: @Composable (Post.Handle) -> Unit
) {
    val context = LocalContext.current
    val component = remember {
        provider.builder(Post.Builder::class.java).build(context)
    }
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
            override fun invoke(post: UiPost, state: EngagementState) {
                when (state) {
                    EngagementState.Like -> engagement(
                        ObserverState.Like(
                            id = post.id,
                            author = post.author.id,
                            message = post.title.text
                        ))
                    EngagementState.Dislike -> engagement(ObserverState.Dislike(post.id))
                    EngagementState.Comment -> engagement(ObserverState.Comment(post.id))
                    EngagementState.View -> engagement(ObserverState.View(post.id))
                }
            }
        } }
        ModerationScreen(
            provider = component,
            viewModelStoreOwner = viewModelStoreOwner
        ) { moderation ->
            val handle = remember { object : Post.Handle {
                override fun component(): Post.Component = component
                override fun engagementOption(): EngagementOption = event
                override fun moderation(): ModerationScreenEvent = moderation
            } }
            updatedContent(handle)
        }
    }
}

@Composable
fun PostScreen(
    id: String,
    limit: Int,
    focused: MutableIntState = rememberSaveable { mutableIntStateOf(-1) },
    listState: LazyListState,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    onFocus: (Int) -> Unit = {},
    connection: @Composable RowScope.(Triple<String, Boolean, Boolean>) -> Unit,
    content: @Composable (Post.Handle) -> Unit
) {
    val updatedContent by rememberUpdatedState(content)
    PostScreen(
        id = id,
        limit = limit,
        provider = provider,
        viewModelStoreOwner = viewModelStoreOwner,
        connection = connection
    ) { handle ->
        PostList(
            listState = listState,
            onFocused = { focused.intValue = it },
            onFocus = onFocus
        ) { updatedContent(handle) }
    }
}

@Composable
fun PostScreen(
    id: String,
    limit: Int,
    pagerState: PagerState,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    content: @Composable PagerScope.(Post.Handle, Int) -> Unit
) {
    val updatedContent by rememberUpdatedState(content)
    PostScreen(
        id = id,
        limit = limit,
        provider = provider,
        viewModelStoreOwner = viewModelStoreOwner,
        connection = {  }
    ) { handle ->
        Column(
            verticalArrangement = Arrangement.Bottom,
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .navigationBarsPadding()
        ) {
            VerticalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) { updatedContent(this, handle, it) }
            Box(modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .background(MaterialTheme.colorScheme.surfaceVariant))
        }
    }
}

