package eu.peernetwork.blog.ui.comment

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.paging.compose.LazyPagingItems
import eu.peernetwork.blog.ui.model.UiComment
import eu.peernetwork.core.common.paging.Pageable
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.luna.DesignPagingStream
import eu.peernetwork.core.ui.design.luna.DesignStreamState
import eu.peernetwork.core.ui.extension.builder
import eu.peernetwork.core.ui.extension.error

@Composable
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)
fun CommentScreen(
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    content: @Composable (Comment.Component, CommentViewModel) -> Unit
) {
    val context = LocalContext.current
    val component = remember { provider.builder(Comment.Builder::class.java).build(context) }
    val viewModel = viewModel(
        modelClass = CommentViewModel::class.java,
        viewModelStoreOwner = viewModelStoreOwner,
        factory = component.viewModelFactory()
    )
    val updatedContent by rememberUpdatedState(content)
    updatedContent(component, viewModel)
}

@Composable
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)
fun CommentScreen(
    id: String,
    limit: Int,
    component: Comment.Component,
    viewModel: CommentViewModel,
    onViewLikes: (String) -> Unit,
    content: @Composable (Comment.Component, CommentInteractor, LazyPagingItems<UiComment>) -> Unit
) {
    val state = viewModel.state.collectAsStateWithLifecycle()
    val status = viewModel.status.collectAsStateWithLifecycle()
    val likes = viewModel.likes.collectAsStateWithLifecycle()
    val isSuccess = remember { derivedStateOf { status.value is CommentViewModel.Status.Success<*> } }
    val derivedState = remember {
        derivedStateOf {
            when (state.value) {
                is CommentViewModel.State.Empty -> DesignStreamState.Default
                is CommentViewModel.State.Loading -> DesignStreamState.Loading
                is CommentViewModel.State.Success -> {
                    val data = (state.value as CommentViewModel.State.Success)
                    DesignStreamState.Success(data.content)
                }
                is CommentViewModel.State.Error -> DesignStreamState.Error(
                    (state.value as CommentViewModel.State.Error).error
                )
            }
        }
    }
    val handleViewLikes by rememberUpdatedState(onViewLikes)
    val updatedContent by rememberUpdatedState(content)
    val interactor = remember { object : CommentInteractor {
        override fun observe(): State<Map<String, UiComment>> = likes

        override fun like(comment: UiComment) {
            viewModel.like(comment)
        }

        override fun viewLike(id: String) {
            handleViewLikes(id)
        }
    } }
    DesignPagingStream(
        state = derivedState,
        loading = { CommentSkeleton(4) },
        error = {
            CommentError(
                isSuccess = isSuccess,
                error = component.resource().error(it.value),
            ) {
                viewModel.load(id, Pageable(0, limit))
            }
        }
    ) { updatedContent(component, interactor, it) }
    LaunchedEffect(Unit) {
        if (state.value is CommentViewModel.State.Empty) {
            viewModel.load(id, Pageable(0, limit))
        }
    }
    DisposableEffect(Unit) {
        onDispose {
            viewModel.reset()
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun CommentScreen(
    id: String,
    limit: Int,
    controller: NavHostController,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    content: LazyListScope.(Comment.Component, CommentInteractor, LazyPagingItems<UiComment>) -> Unit
) {
    val updatedContent by rememberUpdatedState(content)
    CommentScreen(
        provider = provider,
        viewModelStoreOwner = viewModelStoreOwner
    ) { component, viewModel ->
        val status = viewModel.status.collectAsStateWithLifecycle()
        CommentScreen(
            id = id,
            limit = limit,
            component = component,
            viewModel = viewModel,
            onViewLikes = { id -> controller.navigate("likes/$id") }
        ) { component, interactor, items ->
            CommentNavigation(
                limit = limit,
                controller = controller,
                component = component
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) { updatedContent(this, component, interactor, items) }
                LaunchedEffect(status.value) {
                    if (status.value is CommentViewModel.Status.Success<*>) {
                        items.refresh()
                    }
                }
            }
        }
    }
}
