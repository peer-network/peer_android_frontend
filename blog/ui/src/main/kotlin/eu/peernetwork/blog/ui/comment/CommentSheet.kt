package eu.peernetwork.blog.ui.comment

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import eu.peernetwork.blog.ui.extension.route
import eu.peernetwork.blog.ui.model.v2.UiPostDetail
import eu.peernetwork.blog.ui.post.PostNavigator
import eu.peernetwork.blog.ui.post.PostNavigator.Companion.LocalPostNavigator
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.luna.DesignStream
import eu.peernetwork.core.ui.design.luna.DesignStreamState
import eu.peernetwork.core.ui.design.material.DesignBottomSheet
import eu.peernetwork.core.ui.extension.route
import eu.peernetwork.core.ui.extension.value

sealed interface CommentSheetState {
    data object Hidden : CommentSheetState
    data object Visible: CommentSheetState

    data class Dismissing(val action: () -> Unit): CommentSheetState
}

@Composable
fun CommentSheet(
    username: String,
    imageUrl: String,
    state: MutableState<UiPostDetail?>,
    controller: NavHostController = rememberNavController(),
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner
) {
    val showSheet = remember(state.value) { mutableStateOf(state.value != null) }
    val sheetState = remember { mutableStateOf<CommentSheetState>(CommentSheetState.Hidden) }
    val streamState = remember { derivedStateOf {
        if (state.value == null) {
            DesignStreamState.Default
        } else {
            DesignStreamState.Success(state.value!!)
        }
    } }
    val comment = remember { TextFieldState() }
    CommentScreen(
        provider = provider,
        viewModelStoreOwner = viewModelStoreOwner
    ) { component, viewModel ->
        val status = viewModel.status.collectAsStateWithLifecycle()
        val navigator = LocalPostNavigator.current
        val isLoading = remember { derivedStateOf { status.value is CommentViewModel.Status.Loading } }
        val isSuccess = remember { derivedStateOf { status.value is CommentViewModel.Status.Success<*> } }
        DesignStream(streamState) { post ->
            DesignBottomSheet(
                state = showSheet,
                canDismiss = {
                    val dismissable = controller.previousBackStackEntry == null
                    if (!dismissable) {
                        controller.popBackStack()
                    }
                    dismissable
                },
                onDismiss = {
                    if (sheetState.value is CommentSheetState.Dismissing) {
                        (sheetState.value as CommentSheetState.Dismissing).action.invoke()
                    }
                    sheetState.value = CommentSheetState.Hidden
                },
                color = MaterialTheme.colorScheme.surfaceDim,
                footer = {
                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surfaceDim)
                        .navigationBarsPadding()) {
                        CommentForm(
                            username = username,
                            imageUrl = imageUrl,
                            comment = comment,
                            isLoading = isLoading,
                            isSuccess = isSuccess,
                            onSubmit = {
                                viewModel.comment(post.value.id, comment.value)
                            }
                        )
                    }
                },
                content = {
                    Box(modifier = Modifier
                        .fillMaxSize()
                        .statusBarsPadding()
                        .background(MaterialTheme.colorScheme.surfaceDim)) {
                        CommentList(
                            id = post.value.id,
                            limit = 10,
                            controller = controller,
                            provider = provider,
                            viewModelStoreOwner = viewModelStoreOwner,
                            onReply = {
                                comment.edit {
                                    replace(0, length, "@$it")
                                }
                            },
                            onContentClick = { spec, value ->
                                sheetState.value = CommentSheetState.Dismissing {
                                    navigator.navigate(spec.route(value))
                                }
                                showSheet.value = false
                            }
                        ) {
                            sheetState.value = CommentSheetState.Dismissing {
                                navigator.navigate(PostNavigator.Route.Profile(it))
                            }
                            showSheet.value = false
                        }
                    }
                }
            )
            LaunchedEffect(state.value) {
                state.value?.let {
                    sheetState.value = CommentSheetState.Visible
                }
            }
            LaunchedEffect(sheetState.value) {
                if (sheetState.value is CommentSheetState.Hidden) {
                    controller.route("content")
                    state.value = null
                }
            }
        }
    }
}
