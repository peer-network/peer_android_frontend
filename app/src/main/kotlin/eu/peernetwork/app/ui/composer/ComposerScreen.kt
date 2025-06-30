package eu.peernetwork.app.ui.composer

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
import eu.peernetwork.media.ui.attachment.AttachmentScreen
import eu.peernetwork.media.ui.saveable.UiAttachmentSaver
import eu.peernetwork.wallet.ui.model.UiToken

@Composable
fun ComposerScreen(
    provider: UiComponentProvider,
    viewModelStore: ViewModelState,
    onPostSuccess: () -> Unit
) {
    val context = LocalContext.current
    val component = remember {
        provider.builder(Composer.Builder::class.java).build(context)
    }
    val controller = rememberNavController()
    val draft = remember { mutableStateOf<UiDraft?>(null) }
    val attachment = rememberSaveable(saver = UiAttachmentSaver) { mutableStateOf<UiAttachment>(UiAttachment.Text) }
    val focus = remember { FocusRequester() }
    val intent = UiToken.Post
    val key = intent::class.java.name
    Box {
        ComposerNavigation(
            attachment = attachment,
            controller = controller,
            provider = component
        ) {
            ComposerScreen(
                footer = {
                    AttachmentScreen(
                        attachment,
                        onAttach = { controller.navigateIfNecessary("explorer") },
                        component,
                        viewModelStore.get(key),
                        Modifier.padding(top = 4.dp),
                    )
                },
                content = {
                    CreatorScreen(
                        draft = draft,
                        attachment = attachment,
                        focus = focus,
                        provider = component,
                        viewModelStoreOwner = viewModelStore.get(key),
                        modifier = Modifier
                            .padding(top = 8.dp)
                            .padding(horizontal = 16.dp),
                        onSuccess = onPostSuccess
                    )
                    DesignTitleBarHost("CreatorScreen") {
                        titleBar {
                            DesignTitle {
                                Text(stringResource(eu.peernetwork.core.ui.R.string.add_label))
                            }
                        }
                    }
                }
            )
        }
    }
}

@Composable
@SuppressLint("UnusedBoxWithConstraintsScope")
fun ComposerScreen(
    footer: @Composable () -> Unit,
    content: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    val updatedContent by rememberUpdatedState(content)
    val updatedFooter by rememberUpdatedState(footer)
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        updatedFooter()
        updatedContent()
        Spacer(modifier = Modifier.imePadding())
    }
}

@Preview
@Composable
fun PreviewComposerScreen() {
    PeerTheme {
        val focus = remember { FocusRequester() }
        ComposerScreen(
            footer = {
                val state = remember { mutableStateOf<UiAttachment>(UiAttachment.Text) }
                AttachmentScreen(
                    attachment = state,
                    onLoad = { null },
                    onRefresh = {},
                    onAttach = {},
                    onPreview = {},
                    onSelect = {},
                    onSquareClick = {},
                    onPortraitClick = {},
                    onDetach = {}
                )
            },
            content = {
                CreatorScreen(
                    focus = focus,
                    isLoading = remember { mutableStateOf(false) },
                    enabled = remember { mutableStateOf(false) },
                    error = remember { mutableStateOf(null) },
                    shouldReset = remember { mutableStateOf(false) },
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .padding(horizontal = 16.dp)
                ) {}
            }
        )
    }
}
