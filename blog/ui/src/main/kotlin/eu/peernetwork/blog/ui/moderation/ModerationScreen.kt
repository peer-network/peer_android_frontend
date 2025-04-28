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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import eu.peernetwork.core.ui.R
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.compose.DesignTextButton
import eu.peernetwork.core.ui.extension.builder
import androidx.lifecycle.viewmodel.compose.viewModel
import eu.peernetwork.blog.ui.model.UiContent

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
    content(
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
        error?.error?.let {
            Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
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
//            DropdownMenuItem(
//                text = { Text("Save") },
//                onClick = {
//                    expanded = false
//                    viewModel.save(content.id)
//                }
//            )
        }
    }
}
