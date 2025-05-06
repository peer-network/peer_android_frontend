package eu.peernetwork.social.ui.connection

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.extension.builder

interface ConnectionController {
    operator fun invoke()

    fun getOrDefault(id: String, default: Boolean): Boolean
}

@Composable
fun ConnectionScreen(
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    content: @Composable (ConnectionController) -> Unit
) {
    val context = LocalContext.current
    val component = remember {
        provider.builder(Connection.Builder::class.java).build(context)
    }
    val viewModel: ConnectionViewModel = viewModel(
        viewModelStoreOwner = viewModelStoreOwner,
        factory = component.viewModelFactory()
    )
    val state = viewModel.state.collectAsState().value
    val error = remember { derivedStateOf { (state as? ConnectionViewModel.State.Error)?.error } }
     val controller = remember { derivedStateOf {
        object : ConnectionController {
            override fun invoke() {
                TODO("Not yet implemented")
            }

            override fun getOrDefault(id: String, default: Boolean): Boolean {
                TODO("Not yet implemented")
            }
        }
    } }
    LaunchedEffect(error.value) {
        error.value?.let {
            Toast.makeText(context, "Error: ${it.message}", Toast.LENGTH_SHORT).show()
        }
    }
}
