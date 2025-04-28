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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import eu.peernetwork.core.ui.R
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.compose.DesignTextButton
import eu.peernetwork.core.ui.extension.builder
import androidx.lifecycle.viewmodel.compose.viewModel
import eu.peernetwork.blog.ui.model.UiContent

@Composable
fun ModerationActions(
    content: UiContent,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    onActionCompleted: (String) -> Unit = {}
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
    var expanded by remember { mutableStateOf(false) }

    LaunchedEffect(state) {
        when (val currentState = state) {
            is ModerationViewModel.State.Success -> {
                if (currentState.postId == content.id) {
                    onActionCompleted(currentState.postId)
                    Toast.makeText(context, "Action successfully completed", Toast.LENGTH_SHORT).show()
                    viewModel.reset()
                }
            }
            is ModerationViewModel.State.Error -> {
                if (currentState.error != null && currentState.postId == content.id) {
                    Toast.makeText(context, "Error: ${currentState.error.message}", Toast.LENGTH_SHORT).show()
                    viewModel.reset()
                }
            }
            ModerationViewModel.State.Loading -> {
            }
            ModerationViewModel.State.Default -> {
            }
        }
    }

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
                    viewModel.report(content.id)
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