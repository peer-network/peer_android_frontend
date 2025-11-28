package eu.peernetwork.blog.ui.comment

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.clearText
import androidx.compose.material3.MaterialTheme
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
import eu.peernetwork.blog.ui.model.v2.UiPostDetail
import eu.peernetwork.core.ui.design.luna.DesignStream
import eu.peernetwork.core.ui.design.luna.DesignStreamState
import eu.peernetwork.core.ui.design.material.DesignBottomSheet

@Composable
fun CommentSheet(
    state: MutableState<UiPostDetail?>,
    isLoading: State<Boolean>,
    isSuccess: State<Boolean>,
    canDismiss: () -> Boolean,
    onDismiss: () -> Unit,
    onComment: (String, String) -> Unit,
    content: @Composable (UiPostDetail) -> Unit
) {
    val handleDismiss by rememberUpdatedState(onDismiss)
    val handleComment by rememberUpdatedState(onComment)
    val showSheet = remember(state.value) { mutableStateOf(state.value != null) }
    val sheetState = remember { derivedStateOf {
        if (state.value == null) {
            DesignStreamState.Default
        } else {
            DesignStreamState.Success(state.value!!)
        }
    } }
    val comment = remember { TextFieldState() }
    val updatedContent by rememberUpdatedState(content)
    DesignStream(sheetState) { post ->
        DesignBottomSheet(
            state = showSheet,
            canDismiss = canDismiss,
            onDismiss = {
                state.value = null
                handleDismiss()
            },
            color = MaterialTheme.colorScheme.surfaceDim,
            footer = {
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surfaceDim)
                    .navigationBarsPadding()) {
                    CommentForm(
                        username = post.value.username,
                        imageUrl = post.value.imageUrl,
                        comment = comment,
                        isLoading = isLoading,
                        onSubmit = { handleComment(post.value.id, comment.text.toString()) }
                    )
                    LaunchedEffect(isLoading.value) {
                        if (isSuccess.value) {
                            comment.clearText()
                        }
                    }
                }
            },
            content = {
                Box(modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .background(MaterialTheme.colorScheme.surfaceDim)) {
                    updatedContent(post.value)
                }
            }
        )
    }
}
