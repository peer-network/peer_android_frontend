package eu.peernetwork.app.ui.composer

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import eu.peernetwork.blog.ui.creator.CreatorScreen
import eu.peernetwork.blog.ui.model.UiDraft
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.compose.DesignTitle
import eu.peernetwork.core.ui.design.compose.DesignTitleBarHost
import eu.peernetwork.core.ui.extension.builder
import eu.peernetwork.core.ui.extension.navigateIfNecessary
import eu.peernetwork.core.ui.model.ViewModelState
import eu.peernetwork.core.ui.theme.PeerTheme
import eu.peernetwork.media.core.model.UiAttachment
import eu.peernetwork.media.ui.attachment.AttachmentPlaceholder
import eu.peernetwork.media.ui.attachment.AttachmentScreen
import eu.peernetwork.wallet.ui.confirmation.ConfirmationScreen
import eu.peernetwork.wallet.ui.model.UiToken
import kotlinx.coroutines.FlowPreview

@Composable
@OptIn(FlowPreview::class)
fun ComposerScreen(
    provider: UiComponentProvider,
    viewModelStore: ViewModelState
) {
    val context = LocalContext.current
    val component = remember {
        provider.builder(Composer.Builder::class.java).build(context)
    }
    val controller = rememberNavController()
    val draft = remember { mutableStateOf<UiDraft?>(null) }
    val showConfirmation = remember(draft.value) { mutableStateOf(draft.value != null) }
    val attachment = remember { mutableStateOf<UiAttachment>(UiAttachment.Text) }
    val focus = remember { FocusRequester() }
    val intent = UiToken.Post
    val key = intent::class.java.name
    ComposerNavigation(
        attachment = attachment,
        controller = controller,
        provider = component,
    ) {
        ComposerScreen({
            AttachmentScreen(
                attachment,
                { controller.navigateIfNecessary("explorer") },
                component,
                viewModelStore.get(key)
            )
        }) {
            CreatorScreen(
                draft,
                attachment,
                focus,
                component,
                viewModelStore.get(key)
            )
            DesignTitleBarHost("CreatorScreen") {
                titleBar {
                    DesignTitle {
                        Text(stringResource(eu.peernetwork.core.ui.R.string.add_label))
                    }
                }
            }
        }
    }
    ConfirmationScreen(
        intent,
        showConfirmation,
        component,
        viewModelStore.get(key),
        { draft.value = null }
    ) { draft.value = draft.value?.copy(confirmed = it) }
}

@Composable
@SuppressLint("UnusedBoxWithConstraintsScope")
fun ComposerScreen(
    footer: @Composable () -> Unit,
    content: @Composable () -> Unit,
) {
    val updatedContent by rememberUpdatedState(content)
    val updatedFooter by rememberUpdatedState(footer)
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        updatedContent()
        updatedFooter()
    }
}

@Preview
@Composable
fun PreviewComposerScreen() {
    PeerTheme {
        ComposerScreen({
            AttachmentPlaceholder {  }
        }) {}
    }
}
