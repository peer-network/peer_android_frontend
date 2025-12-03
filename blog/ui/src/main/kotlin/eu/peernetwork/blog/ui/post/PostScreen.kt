package eu.peernetwork.blog.ui.post

import android.graphics.Bitmap
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.pager.PagerScope
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import eu.peernetwork.blog.ui.comment.CommentToolbar
import eu.peernetwork.blog.ui.engagement.EngagementInteractor
import eu.peernetwork.blog.ui.engagement.EngagementInteractor.Companion.LocalEngagementInteractor
import eu.peernetwork.blog.ui.engagement.EngagementInteractor.State as EngagementState
import eu.peernetwork.blog.ui.engagement.EngagementScreen
import eu.peernetwork.blog.ui.engagement.EngagementReaction
import eu.peernetwork.blog.ui.engagement.EngagementReaction.Companion.LocalEngagementReaction
import eu.peernetwork.blog.ui.mapper.v2.mapToDetail
import eu.peernetwork.blog.ui.mapper.v2.mapToEngagement
import eu.peernetwork.blog.ui.engagement.EngagementReaction.State as ReactionState
import eu.peernetwork.blog.ui.model.v2.UiPost
import eu.peernetwork.blog.ui.model.v2.UiPostDetail
import eu.peernetwork.blog.ui.moderation.ModerationScreen
import eu.peernetwork.blog.ui.post.PostInteractor.Companion.LocalPostInteractor
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.extension.builder

@Composable
fun PostScreen(
    limit: Int,
    username: String,
    imageUrl: String,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val component = remember {
        provider.builder(Post.Builder::class.java).build(context)
    }
    val viewModel = viewModel(
        modelClass = PostViewModel::class.java,
        viewModelStoreOwner = viewModelStoreOwner,
        factory = component.viewModelFactory()
    )
    val updatedContent by rememberUpdatedState(content)
    val thumbnail = viewModel.thumbnail.collectAsStateWithLifecycle()
    EngagementScreen(
        limit = limit,
        username = username,
        imageUrl = imageUrl,
        provider = component,
        viewModelStoreOwner = viewModelStoreOwner
    ) {
        val engagement = LocalEngagementInteractor.current
        val reaction = remember { object : EngagementReaction {
            override fun invoke(post: UiPost, state: ReactionState) {
                when (state) {
                    ReactionState.Like -> engagement(
                        EngagementState.Like(
                            id = post.id,
                            author = post.author.id,
                            message = post.title.text
                        ))
                    ReactionState.Dislike -> engagement(EngagementState.Dislike(post.id))
                    ReactionState.Comment -> {
                        engagement(EngagementState.Comment(post.mapToDetail()))
                    }
                    ReactionState.View -> {
                        engagement(EngagementState.View(post.mapToEngagement()))
                    }
                }
            }
        } }
        ModerationScreen(
            provider = component,
            viewModelStoreOwner = viewModelStoreOwner
        ) {
            val interactor = remember { object : PostInteractor {
                override fun observe(): State<Map<String, Bitmap?>> = thumbnail

                override fun component(): Post.Component = component

                override fun background(
                    media: String,
                    aspectRatio: Float,
                    width: Int,
                    height: Int,
                    fit: Boolean
                ) {
                    viewModel.videoBackground(
                        media = media,
                        aspectRatio = aspectRatio,
                        width = width,
                        height = height,
                        fit = fit
                    )
                }
            } }
            CompositionLocalProvider(
                LocalPostInteractor provides interactor,
                LocalEngagementReaction provides reaction,
            ) { updatedContent() }
        }
    }
}

@Composable
fun PostScreen(
    limit: Int,
    username: String,
    imageUrl: String,
    focused: MutableIntState,
    listState: LazyListState,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    onFocus: (Int) -> Unit = {},
    content: @Composable (State<Int>) -> Unit
) {
    val updatedContent by rememberUpdatedState(content)
    PostScreen(
        limit = limit,
        username = username,
        imageUrl = imageUrl,
        provider = provider,
        viewModelStoreOwner = viewModelStoreOwner,
    ) {
        PostList(
            listState = listState,
            onFocused = { focused.intValue = it },
            onFocus = onFocus
        ) { updatedContent(it) }
    }
}

@Composable
fun PostScreen(
    username: String,
    imageUrl: String,
    limit: Int,
    pagerState: PagerState,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    onComment: (Int) -> UiPostDetail?,
    content: @Composable PagerScope.(Int) -> Unit
) {
    val handleComment by rememberUpdatedState(onComment)
    val updatedContent by rememberUpdatedState(content)
    PostScreen(
        limit = limit,
        username = username,
        imageUrl = imageUrl,
        provider = provider,
        viewModelStoreOwner = viewModelStoreOwner,
    ) {
        val interactor = LocalEngagementInteractor.current
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
            ) { updatedContent(this, it) }
            CommentToolbar(
                username = username,
                imageUrl = imageUrl,
                onClick = {
                    handleComment(pagerState.currentPage)?.let {
                        interactor(EngagementInteractor.State.Comment(it))
                    }
                }
            )
        }
    }
}
