package eu.peernetwork.user.ui.v2.login

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.extension.builder

@Composable
fun LoginScreen(
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    onPasswordReset: (String) -> Unit,
    onPrivacy: () -> Unit,
    onRegister: () -> Unit
) {
    val context = LocalContext.current
    val component = remember {
        provider.builder(Login.Builder::class.java).build(context)
    }
    val viewModel = viewModel(
        modelClass = LoginViewModel::class.java,
        viewModelStoreOwner = viewModelStoreOwner,
        factory = component.viewModelFactory()
    )
    val state by viewModel.state.collectAsStateWithLifecycle()
    val isLoading = remember { derivedStateOf { state is LoginViewModel.State.Loading } }
    val error = remember { derivedStateOf {
        (state as? LoginViewModel.State.Error?)?.error?.message?.let {
            component.resource().string(it)
        }
    } }
    LoginPage(
        isLoading = isLoading,
        error = error,
        onLogin = { email, password -> viewModel.login(email, password) },
        onRegister = onRegister,
        onPasswordReset = onPasswordReset,
        onPrivacy = onPrivacy,
    )
    DisposableEffect(Unit) { onDispose { viewModel.reset() } }
}
