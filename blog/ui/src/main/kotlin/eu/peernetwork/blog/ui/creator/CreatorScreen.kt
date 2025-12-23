package eu.peernetwork.blog.ui.creator

import android.widget.Toast
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import eu.peernetwork.blog.ui.R
import eu.peernetwork.blog.ui.engagement.EngagementDialog
import eu.peernetwork.blog.ui.engagement.EngagementIntent
import eu.peernetwork.blog.ui.model.UiDraft
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.extension.builder
import eu.peernetwork.media.core.model.UiAttachment
import eu.peernetwork.media.core.model.UiMimeType

@Composable
fun CreatorScreen(
    title: TextFieldState,
    description: TextFieldState,
    attachment: MutableState<UiAttachment>,
    focus: FocusRequester,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    modifier: Modifier = Modifier,
    onClear: () -> Unit = {},
    onSuccess: () -> Unit = {},
) {
    val context = LocalContext.current
    val component = remember {
        provider.builder(Creator.Builder::class.java).build(context)
    }
    val viewModel = viewModel(
        modelClass = CreatorViewModel::class.java,
        viewModelStoreOwner = viewModelStoreOwner,
        factory = component.viewModelFactory()
    )
    val state by viewModel.state.collectAsStateWithLifecycle()
    val error = remember { derivedStateOf {
        (state as? CreatorViewModel.State.Error?)?.error?.message?.let {
            component.resource().string(it)
        }
    } }
    val isLoading = remember { derivedStateOf {
        state is CreatorViewModel.State.Loading
    } }
    val shouldReset = remember { derivedStateOf {
        state is CreatorViewModel.State.Success
    } }
    val media = remember(attachment.value) { derivedStateOf {
        attachment.value.media
    } }
    val enabled = remember(attachment.value) { derivedStateOf {
        when {
            media.value == UiMimeType.Text ->
                title.text.isNotBlank() || description.text.isNotBlank()
            media.value == UiMimeType.Music ->
                attachment.value.files.isNotEmpty() &&
                        attachment.value.files.any { it.cover != null }
            else -> attachment.value.files.isNotEmpty()
        }
    } }
    val draft = remember { mutableStateOf<UiDraft?>(null) }
    val type = remember { mutableStateOf<EngagementIntent?>(null) }
    val handleOnClear by rememberUpdatedState(onClear)
    val handleOnSuccess by rememberUpdatedState(onSuccess)
    val successMessage = stringResource(R.string.post_success_message)
    CreatorPage(
        title = title,
        description = description,
        focus = focus,
        onSubmit = {
            val post = UiDraft(
                title = it.title,
                description = it.description,
                attachment = attachment.value
            )
            draft.value = post
            type.value = EngagementIntent.Post(post)
        },
        isLoading = isLoading,
        enabled = enabled,
        error = error,
        modifier = modifier
    )
    component.engagementConfirmation()(
        modifier = Modifier,
        spec = EngagementDialog.Spec(
            type = type,
            viewModelStoreOwner = viewModelStoreOwner,
            onDismiss = {
                type.value = null
                draft.value = null
            }
        ) {
            when(it) {
                is EngagementIntent.Post -> {
                    viewModel.create(it.draft)
                }
                else -> {}
            }
            type.value = null
            draft.value = null
        }
    )
    LaunchedEffect(shouldReset.value) {
        if (shouldReset.value) {
            viewModel.reset()
            handleOnClear()
            attachment.value = UiAttachment.Text
            Toast.makeText(context, successMessage, Toast.LENGTH_SHORT).show()
            handleOnSuccess()
        }
    }
    DisposableEffect(Unit) {
        onDispose { viewModel.reset() }
    }
}
