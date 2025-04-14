package eu.peernetwork.blog.ui.creator

import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.clearText
import androidx.compose.material3.Icon
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import eu.peernetwork.blog.ui.author.AuthorScreen
import eu.peernetwork.blog.ui.model.UiDraft
import eu.peernetwork.core.ui.theme.PeerTheme
import eu.peernetwork.core.ui.R
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.compose.DesignButton
import eu.peernetwork.core.ui.design.compose.DesignCard
import eu.peernetwork.core.ui.design.compose.DesignCheckButton
import eu.peernetwork.core.ui.design.compose.DesignLabel
import eu.peernetwork.core.ui.design.compose.DesignToolbarTitle
import eu.peernetwork.core.ui.extension.builder
import eu.peernetwork.core.ui.extension.isValidInput
import eu.peernetwork.media.core.model.MimeType
import eu.peernetwork.media.core.renderer.MediaSelector

@Composable
fun CreatorScreen(
    title: MutableState<DesignToolbarTitle>,
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
    val attachments = remember { mutableStateOf<List<Uri>>(emptyList()) }
    CreatorContent(
        attachments = attachments,
        onSubmit = { viewModel.create(it) },
        header = { AuthorScreen(component, viewModelStoreOwner) },
        footer = {
            component.mediaSelector()(
                modifier = Modifier,
                spec = MediaSelector.Spec(it, attachments)
            ) },
        isLoading = isLoading,
        shouldReset = shouldReset,
        error = error
    )
    LaunchedEffect(Unit) { title.value = DesignToolbarTitle(R.string.add_label) }
}

@Composable
fun CreatorContent(
    isLoading: State<Boolean>,
    shouldReset: State<Boolean>,
    error: State<Throwable?>,
    modifier: Modifier = Modifier,
    attachments: MutableState<List<Uri>>,
    onSubmit: (UiDraft) -> Unit = {},
    header: @Composable () -> Unit = {},
    footer: @Composable (MimeType) -> Unit = {}
) {
    val title = remember { TextFieldState() }
    val description = remember { TextFieldState() }
    val selected = remember { mutableStateOf<MimeType?>(null) }
    val state = remember { mutableStateOf<MimeType?>(null) }
    CreatorScaffold(
        isLoading = isLoading,
        type = selected,
        modifier = modifier.fillMaxSize(),
        footer = { footer(state.value ?: MimeType.Photo) },
    ) {
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
            modifier = Modifier.padding(bottom = 6.dp)
        ) {
            DesignCard(
                shape = RoundedCornerShape(24.dp),
                color = MaterialTheme.colorScheme.surfaceVariant,
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .padding(bottom = 8.dp)
            ) {
                Column {
                    CreatorForm(title, description, isLoading, header)
                    CreatorActions(
                        title = title,
                        description = description,
                        isLoading = isLoading,
                        selected = selected,
                        state = state,
                        attachments = attachments,
                        onSubmit = onSubmit,
                    )
                }
            }
        }
    }
    LaunchedEffect(shouldReset.value) {
        if (shouldReset.value) {
            title.clearText()
            description.clearText()
            attachments.value = emptyList()
        }
    }
}

@Composable
private fun CreatorActions(
    title: TextFieldState,
    description: TextFieldState,
    isLoading: State<Boolean>,
    selected: MutableState<MimeType?>,
    state: MutableState<MimeType?>,
    attachments: MutableState<List<Uri>>,
    onSubmit: (UiDraft) -> Unit = {},
) {
    val isFormValid = remember { derivedStateOf {
        title.isValidInput() && description.isValidInput()
                && (selected.value?.let { it !is MimeType.Text
                && attachments.value.isNotEmpty() } == true || selected.value == null)
    } }
    Row(verticalAlignment = Alignment.CenterVertically) {
        MimeType.TYPES.forEach {
            val buttonState = remember(selected.value) {
                mutableStateOf(selected.value == it)
            }
            DesignCheckButton(
                check = buttonState,
                onCheck = { isChecked ->
                    attachments.value = emptyList()
                    state.value = it
                    selected.value = if (isChecked) {
                        it
                    } else {
                        null
                    }
                }
            ) {
                Icon(
                    painter = painterResource(it.id),
                    contentDescription = it.label?.let { stringResource(it) },
                )
            }
            Spacer(modifier = Modifier.width(4.dp))
        }
        Spacer(modifier = Modifier.weight(1f))
        DesignButton(
            onClick = {
                onSubmit(UiDraft(
                    title.text.toString(),
                    description.text.toString(),
                    selected.value ?: if (attachments.value.isEmpty()) {
                        MimeType.Text
                    } else {
                        MimeType.Photo
                    },
                    attachments.value)
                ) },
            isLoading = isLoading.value,
            enabled = isFormValid.value,
            shape = RoundedCornerShape(10.dp),
            contentPadding = PaddingValues(vertical = 4.dp, horizontal = 32.dp),
            modifier = Modifier.height(36.dp),
        ) {
            Text(
                stringResource(eu.peernetwork.blog.ui.R.string.post_label),
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Bold
                )
            )
        }
    }
}

@Preview
@Composable
fun PreviewCreatorScreen() {
    PeerTheme {
        CreatorContent(
            isLoading = remember { mutableStateOf(false) },
            shouldReset = remember { mutableStateOf(false) },
            error = remember { mutableStateOf(null) },
            attachments = remember { mutableStateOf<List<Uri>>(emptyList()) },
        ) {}
    }
}
