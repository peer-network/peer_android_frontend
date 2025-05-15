package eu.peernetwork.blog.ui.moderation

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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import eu.peernetwork.blog.ui.model.UiContent
import eu.peernetwork.core.ui.R
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.compose.DesignTextButton
import eu.peernetwork.core.ui.extension.builder

data class ModerationSpec(
    val onReport: (String) -> Unit,
    val onSave: (String) -> Unit = {}
)

@Composable
fun ModerationScreen(
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    content: @Composable (ModerationSpec) -> Unit
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
    updatedContent(
        ModerationSpec(
            onSave = { viewModel.save(it) },
            onReport = { viewModel.report(it) }
        )
    )
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
    spec: ModerationSpec
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        DesignTextButton(
            onClick = { expanded = true },
            contentPadding = PaddingValues(0.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_menu),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.tertiary,
                modifier = Modifier.size(28.dp)
            )
        }

        DropdownMenu(
            modifier = Modifier.background(color = MaterialTheme.colorScheme.background),
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text("Report") },
                onClick = {
                    expanded = false
                    spec.onReport(model.id)
                }
            )
// Uncomment this if you want to allow Save option later
//            DropdownMenuItem(
//                text = { Text("Save") },
//                onClick = {
//                    expanded = false
//                    spec.onSave(model.id)
//                }
//            )
        }
    }
}
