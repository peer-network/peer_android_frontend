package eu.peernetwork.blog.ui.creator

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import eu.peernetwork.blog.ui.R
import eu.peernetwork.blog.ui.engagement.EngagementConfirmation
import eu.peernetwork.blog.ui.engagement.EngagementType
import eu.peernetwork.blog.ui.model.UiDraft
import eu.peernetwork.core.ui.theme.PeerTheme
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.compose.DesignLabel
import eu.peernetwork.core.ui.extension.builder
import eu.peernetwork.media.core.model.UiAttachment
import eu.peernetwork.media.core.model.UiMimeType

@Composable
fun CreatorScreen(
    draft: MutableState<UiDraft?>,
    attachment: MutableState<UiAttachment>,
    focus: FocusRequester,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    modifier: Modifier = Modifier,
    onSuccess: () -> Unit = {}
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
        if (attachment.value.files.isEmpty()) {
            UiMimeType.Text
        } else { attachment.value.media }
    } }
    val enabled = remember(attachment.value) { derivedStateOf {
        if (media.value != UiMimeType.Text) {
            true
        } else {
            attachment.value.files.isNotEmpty()
        }
    } }
    val type = remember(draft.value) {
        mutableStateOf<EngagementType?>(draft.value?.let { EngagementType.Post(it) })
    }
    val handleOnSuccess by rememberUpdatedState(onSuccess)
    val successMessage = stringResource(R.string.post_success_message)
    CreatorScreen(
        focus = focus,
        onSubmit = {
            draft.value = UiDraft(
                title = it.title,
                description = it.description,
                media = if (attachment.value.files.isEmpty()) {
                    UiMimeType.Text
                } else { attachment.value.media },
                attachments = attachment.value.files.map { file -> file.uri }
            ) },
        onReset = { attachment.value = UiAttachment.Text },
        isLoading = isLoading,
        enabled = enabled,
        shouldReset = shouldReset,
        error = error,
        modifier = modifier
    )
    component.engagementConfirmation()(
        Modifier,
        EngagementConfirmation.Spec(
            type,
            viewModelStoreOwner,
        ) {
            when(it) {
                is EngagementType.Post -> {
                    viewModel.create(it.draft)
                    draft.value = null
                }
                else -> {}
            }
        }
    )
    LaunchedEffect(shouldReset.value) {
        if (shouldReset.value) {
            viewModel.reset()
            Toast.makeText(context, successMessage, Toast.LENGTH_SHORT).show()
            handleOnSuccess()
        }
    }
    DisposableEffect(Unit) {
        onDispose { viewModel.reset() }
    }
}

@Composable
fun CreatorScreen(
    isLoading: State<Boolean>,
    focus: FocusRequester,
    enabled: State<Boolean>,
    shouldReset: State<Boolean>,
    error: State<String?>,
    modifier: Modifier = Modifier,
    onReset: () -> Unit = { },
    onSubmit: (UiDraft.Field) -> Unit = { },
) {
    var title by rememberSaveable(stateSaver = TextFieldState.Saver) { mutableStateOf(TextFieldState()) }
    var description by rememberSaveable(stateSaver = TextFieldState.Saver) {
        mutableStateOf(TextFieldState())
    }
    val handleOnReset by rememberUpdatedState(onReset)
    Column(
        modifier = modifier
            .padding(horizontal = 8.dp)
            .padding(top = 8.dp)
    ) {
        DesignLabel(
            label = { error.value?.let {
                Text(
                    it,
                    modifier = Modifier.padding(horizontal = 16.dp)
                        .padding(vertical = 8.dp),
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.error
                    )
                )
            }},
            visible = error.value != null,
            modifier = Modifier.padding(bottom = 4.dp)
        ) {
            CreatorForm(title, focus, description, isLoading)
        }
        Spacer(modifier = Modifier.height(8.dp))
        CreatorFooter(
            title = title,
            description = description,
            isLoading = isLoading,
            enabled = enabled,
            onSubmit = onSubmit,
        )
    }
    LaunchedEffect(shouldReset.value) {
        if (shouldReset.value) {
            title = TextFieldState()
            description = TextFieldState()
            handleOnReset()
        }
    }
}

@Preview
@Composable
fun PreviewCreatorScreen() {
    PeerTheme {
        val focus = remember { FocusRequester() }
        CreatorScreen(
            focus = focus,
            isLoading = remember { mutableStateOf(false) },
            enabled = remember { mutableStateOf(false) },
            error = remember { mutableStateOf(null) },
            shouldReset = remember { mutableStateOf(false) },
        ) {}
    }
}
