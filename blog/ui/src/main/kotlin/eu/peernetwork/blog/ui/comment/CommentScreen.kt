package eu.peernetwork.blog.ui.comment

import android.widget.Toast
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.clearText
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import eu.peernetwork.blog.domain.model.Engagement
import eu.peernetwork.blog.ui.compose.Placeholder
import eu.peernetwork.blog.ui.interaction.listing.ListingScreen
import eu.peernetwork.blog.ui.model.UiContent
import eu.peernetwork.core.common.paging.Pageable
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.component.DesignPagingScaffold
import eu.peernetwork.core.ui.design.component.DesignStatefulScaffoldState
import eu.peernetwork.core.ui.design.compose.DesignRouter
import eu.peernetwork.core.ui.extension.builder
import eu.peernetwork.core.ui.extension.navigateIfNecessary
import eu.peernetwork.core.ui.extension.route

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)
@Composable
fun CommentScreen(
    state: MutableState<UiContent?>,
    postLimit: Int,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    onMentionClick: (String) -> Unit = {},
    onHashtagClick: (String) -> Unit = {},
    onAuthorClick: (String) -> Unit = {},
    connection: @Composable RowScope.(Triple<String, Boolean, Boolean>) -> Unit = {}
) {
    val context = LocalContext.current
    val component = remember { provider.builder(Comment.Builder::class.java).build(context) }
    val viewModel = viewModel(
        modelClass = CommentViewModel::class.java,
        viewModelStoreOwner = viewModelStoreOwner,
        factory = component.viewModelFactory()
    )
    val sheetState = viewModel.state.collectAsStateWithLifecycle()
    val sheetStatus = viewModel.status.collectAsStateWithLifecycle()
    val likesState = viewModel.likes.collectAsStateWithLifecycle()
    val derivedState = remember {
        derivedStateOf {
            when (sheetState.value) {
                is CommentViewModel.State.Empty -> DesignStatefulScaffoldState.Empty
                is CommentViewModel.State.Loading -> DesignStatefulScaffoldState.Loading
                is CommentViewModel.State.Success -> {
                    DesignStatefulScaffoldState.Success(
                        (sheetState.value as CommentViewModel.State.Success).content
                    )
                }
                is CommentViewModel.State.Error -> DesignStatefulScaffoldState.Error(
                    (sheetState.value as CommentViewModel.State.Error).error
                )
            }
        }
    }
    val isLoading = remember {
        derivedStateOf {
            sheetStatus.value is CommentViewModel.Status.Loading
        }
    }
    val error = remember {
        derivedStateOf {
            (sheetStatus.value as? CommentViewModel.Status.Error?)
                ?.error?.message
        }
    }
    val isCommentPosted = remember {
        derivedStateOf {
            (sheetStatus.value as? CommentViewModel.Status.Success<*>?)
                ?.intent == CommentViewModel.Intent.Comment
        }
    }
    val replyTo = remember { mutableStateOf<String?>(null) }
    val handleOnMentionClick by rememberUpdatedState(onMentionClick)
    val handleOnHashtagClick by rememberUpdatedState(onHashtagClick)
    val handleOnAuthorClick by rememberUpdatedState(onAuthorClick)
    val action = remember { mutableStateOf<(() -> Unit)?>(null) }
    val controller = rememberNavController()
    CommentScreen(
        state = state,
        replyTo = replyTo,
        isLoading = isLoading,
        controller = controller,
        onDismiss = {
            action.value?.invoke()
            action.value = null
            controller.route("content")},
        onMentionClick = { username ->
            action.value = { handleOnMentionClick(username) }
            state.value = null
        },
        onHashtagClick = { hashtag ->
            action.value = { handleOnHashtagClick(hashtag) }
            state.value = null
        },
        onSubmit = { id, comment -> viewModel.comment(id, comment) },
        likes = { id, entry, size ->
            ListingScreen(
                id = id,
                postLimit = postLimit,
                engagement = Engagement.Content.LikedComment,
                provider = component,
                onAuthorClick = {
                    action.value = { handleOnAuthorClick(it) }
                    state.value = null
                },
                viewModelStoreOwner = entry,
                connection = connection
            )
        }
    ) { size, field ->
        DesignPagingScaffold(
            state = derivedState,
            onRefresh = { state.value?.let { viewModel.load(it.id, Pageable(0, postLimit)) } },
            placeholder = { Placeholder(modifier = Modifier.padding(horizontal = 24.dp)) }
        ) { pageState, items ->
            CommentListing(
                likes = likesState,
                lazyPagingItems = items,
                size = size.value,
                onLike = { viewModel.like(it) },
                onComment = { controller.navigateIfNecessary("likes/${it.id}") },
                titleOnClick = { replyTo.value = it },
                onMentionClick = { username ->
                    action.value = { handleOnMentionClick(username) }
                    state.value = null
                },
                onHashtagClick = { hashtag ->
                    action.value = { handleOnHashtagClick(hashtag) }
                    state.value = null
                },
                onAuthorClick = {
                    action.value = { handleOnAuthorClick(it) }
                    state.value = null
                },
            )
            LaunchedEffect(isLoading.value) {
                if (isCommentPosted.value) {
                    field.clearText()
                    items.refresh()
                }
            }
        }
        LaunchedEffect(error.value) {
            if (error.value != null) {
                Toast.makeText(
                    context,
                    error.value?.let { component.resource().string(it) },
                    Toast.LENGTH_SHORT
                ).show()
                viewModel.clear()
            }
        }
        LaunchedEffect(state.value) {
            if (state.value != null) {
                state.value?.let { viewModel.load(it.id, Pageable(0, postLimit)) }
            } else {
                viewModel.reset()
            }
        }
    }
}

@Composable
fun CommentScreen(
    state: MutableState<UiContent?>,
    replyTo: MutableState<String?>,
    isLoading: State<Boolean>,
    controller: NavHostController,
    onDismiss: () -> Unit = {},
    onMentionClick: (String) -> Unit = {},
    onHashtagClick: (String) -> Unit = {},
    onSubmit: (String, String) -> Unit = { id, comment -> },
    likes: @Composable (String, NavBackStackEntry, State<Size>) -> Unit = { id, entry, size -> },
    content: @Composable (State<Size>, TextFieldState) -> Unit = { size, field -> }
) {
    val comment = remember { TextFieldState() }
    val updatedLikes by rememberUpdatedState(likes)
    val updatedContent by rememberUpdatedState(content)
    CommentScaffold(
        state = state,
        onDismiss = onDismiss,
        canDismiss = {
            val canDismiss = controller.previousBackStackEntry == null
            if (!canDismiss) {
                controller.popBackStack()
            }
            canDismiss
        },
        sheet = {
            val content = remember { mutableStateOf(state.value) }
            content.value?.let {
                CommentForm(
                    model = it,
                    comment = comment,
                    replyTo = replyTo,
                    isLoading = isLoading,
                    modifier = Modifier.padding(horizontal = 24.dp),
                    onSubmit = onSubmit,
                    onMentionClick = onMentionClick,
                    onHashtagClick = onHashtagClick
                )
            } },
        content = { size ->
            DesignRouter(
                navController = controller,
                startDestination = "content"
            ) {
                composable(route = "content") { updatedContent(size, comment) }
                composable(
                    route = "likes/{id}",
                    arguments = listOf(navArgument("id") {
                        type = NavType.StringType
                    })
                ) { backStackEntry ->
                    val id = backStackEntry.arguments?.getString("id") ?: ""
                    updatedLikes(id, backStackEntry, size)
                }
            }
        }
    )
}
