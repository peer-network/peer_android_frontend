package eu.peernetwork.blog.ui.creator

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import eu.peernetwork.blog.ui.author.AuthorScreen
import eu.peernetwork.blog.ui.model.UiDraft
import eu.peernetwork.core.ui.theme.PeerTheme
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.compose.DesignCard
import eu.peernetwork.core.ui.design.compose.DesignLabel
import eu.peernetwork.core.ui.extension.builder
import eu.peernetwork.media.core.model.UiAttachment
import eu.peernetwork.media.core.model.UiMimeType

@Composable
fun CreatorScreen(
    attachment: MutableState<UiAttachment>,
    focus: FocusRequester,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
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
        (state as? CreatorViewModel.State.Error?)?.error
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
    CreatorScreen(
        focus = focus,
        onSubmit = {
            viewModel.create(UiDraft(
                title = it.title,
                description = it.description,
                media = if (attachment.value.files.isEmpty()) {
                    UiMimeType.Text
                } else { attachment.value.media },
                attachments = attachment.value.files.map { it.uri }
            )) },
        header = { AuthorScreen(component, viewModelStoreOwner) },
        isLoading = isLoading,
        enabled = enabled,
        shouldReset = shouldReset,
        attachment = attachment,
        error = error
    )
    LaunchedEffect(shouldReset.value) {
        if (shouldReset.value) {
            viewModel.reset()
        }
    }
}

@Composable
fun CreatorScreen(
    isLoading: State<Boolean>,
    focus: FocusRequester,
    enabled: State<Boolean>,
    shouldReset: State<Boolean>,
    attachment: MutableState<UiAttachment>,
    error: State<Throwable?>,
    modifier: Modifier = Modifier,
    onSubmit: (UiDraft.Field) -> Unit = {},
    header: @Composable () -> Unit = {}
) {
    val title = rememberSaveable(stateSaver = TextFieldState.Saver) { mutableStateOf(TextFieldState()) }
    val description = rememberSaveable(stateSaver = TextFieldState.Saver) {
        mutableStateOf(TextFieldState())
    }
    DesignLabel(
        label = { error.value?.message?.let {
            Text(it,
                modifier = Modifier.padding(horizontal = 24.dp)
                    .padding(vertical = 8.dp),
                style = MaterialTheme.typography.bodySmall.copy(
                    color = MaterialTheme.colorScheme.error
                )
            )
        }},
        visible = error.value != null,
        modifier = modifier.padding(bottom = 4.dp)
    ) {
        DesignCard(
            shape = RoundedCornerShape(24.dp),
            color = MaterialTheme.colorScheme.surfaceVariant,
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .padding(top = 8.dp)
        ) {
            Column {
                CreatorForm(title.value, focus, description.value, isLoading, header)
                CreatorFooter(
                    title = title.value,
                    description = description.value,
                    isLoading = isLoading,
                    enabled = enabled,
                    onSubmit = onSubmit,
                )
            }
        }
    }
    LaunchedEffect(shouldReset.value) {
        if (shouldReset.value) {
            title.value = TextFieldState()
            description.value = TextFieldState()
            attachment.value = UiAttachment.Text
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
            attachment = remember { mutableStateOf(UiAttachment.Text) },
        ) {}
    }
}
