package eu.peernetwork.blog.ui.moderation

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import eu.peernetwork.blog.ui.event.UiModerationEvent
import eu.peernetwork.blog.ui.model.UiContent
import eu.peernetwork.core.ui.R
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.material.DesignTextButton
import eu.peernetwork.core.ui.extension.builder

@Composable
fun ModerationScreen(
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    content: @Composable (UiModerationEvent) -> Unit
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
        UiModerationEvent(
            onSave = { viewModel.save(it) },
            onReport = { viewModel.report(it) }
        )
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

@Composable
fun ModerationScreen(
    model: UiContent,
    event: UiModerationEvent,
    size: Dp = 24.dp,
    color: Color = MaterialTheme.colorScheme.tertiary,
) {
    var expanded by remember { mutableStateOf(false) }
    val handleOnReport by rememberUpdatedState(event.onReport)
    val context = LocalContext.current
    Box {
        DesignTextButton(
            onClick = { expanded = true },
            contentPadding = PaddingValues(0.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_menu),
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(size)
            )
        }
        DropdownMenu(
            modifier = Modifier.background(MaterialTheme.colorScheme.background),
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text("Report") },
                onClick = {
                    expanded = false
                    handleOnReport(model.id)
                }
            )
            DropdownMenuItem(
                text = { Text("Share") },
                onClick = {
                    expanded = false
                    model.url?.let { url ->
                        val shareIntent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_TEXT, url)
                        }
                        val chooser = Intent.createChooser(shareIntent, "Share via")
                        context.startActivity(chooser)
                    }
                }
            )
        }
    }
}
