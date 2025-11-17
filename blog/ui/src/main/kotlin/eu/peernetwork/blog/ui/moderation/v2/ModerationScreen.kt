package eu.peernetwork.blog.ui.moderation.v2

import android.widget.Toast
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import eu.peernetwork.blog.ui.moderation.Moderation
import eu.peernetwork.blog.ui.moderation.ModerationViewModel
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.extension.builder

interface ModerationScreenEvent {
    fun onReport(id: String)
}

@Composable
fun ModerationScreen(
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    content: @Composable (ModerationScreenEvent) -> Unit
) {
    val context = LocalContext.current
    val component = remember {
        provider.builder(Moderation.Builder::class.java).build(context)
    }
    val viewModel = viewModel(
        modelClass = ModerationViewModel::class.java,
        viewModelStoreOwner = viewModelStoreOwner,
        factory = component.viewModelFactory()
    )
    val state by viewModel.state.collectAsStateWithLifecycle()
    val error by remember { derivedStateOf { state as? ModerationViewModel.State.Error? } }
    val success by remember { derivedStateOf { state as? ModerationViewModel.State.Success? } }
    val message = stringResource(eu.peernetwork.blog.ui.R.string.action_message)
    val updatedContent by rememberUpdatedState(content)
    val event = remember(state) {
        object : ModerationScreenEvent {
            override fun onReport(id: String) {
                viewModel.report(id)
            }
        }
    }
    updatedContent(event)
    LaunchedEffect(error, success) {
        success?.postId?.let {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            viewModel.reset()
        }
        error?.error?.message?.let {
            Toast.makeText(context, component.resource().string(it), Toast.LENGTH_SHORT).show()
            viewModel.reset()
        }
    }
}
