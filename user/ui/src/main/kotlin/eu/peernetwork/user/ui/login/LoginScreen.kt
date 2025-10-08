package eu.peernetwork.user.ui.login

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.material.DesignButton
import eu.peernetwork.core.ui.extension.builder
import eu.peernetwork.core.ui.theme.PeerTheme
import eu.peernetwork.user.ui.R
import eu.peernetwork.core.ui.extension.isValidEmail
import eu.peernetwork.core.ui.extension.isValidInput

@Composable
fun LoginScreen(
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    onForgotPassword: (String) -> Unit,
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
    val loading = remember { derivedStateOf { state is LoginViewModel.State.Loading } }
    val error = remember { derivedStateOf {
        (state as? LoginViewModel.State.Error?)?.error?.message?.let {
            component.resource().string(it)
        }
    } }
    LoginScreen(
        loading = loading,
        error = error,
        onReset = { viewModel.reset() },
        onForgotPassword = onForgotPassword,
    ) { email, password -> viewModel.login(email, password) }
}

@Composable
fun LoginScreen(
    loading: State<Boolean>,
    error: State<String?>,
    onReset: (() -> Unit)? = null,
    onForgotPassword: (String) -> Unit,
    onSubmit: (String, String) -> Unit
) {
    val password = remember { TextFieldState() }
    val email by rememberSaveable(stateSaver = TextFieldState.Saver) { mutableStateOf(TextFieldState()) }
    val validate by remember(email, password) { derivedStateOf {
        email.isValidEmail() && password.isValidInput()
    } }
    val handleReset by rememberUpdatedState(onReset)
    val handleSubmit by rememberUpdatedState(onSubmit)
    Column(modifier = Modifier.fillMaxWidth()) {
        LoginForm(
            email = email,
            password = password,
            error = error.value,
            enabled = !loading.value,
            onForgotPassword = onForgotPassword
        )
        DesignButton(
            enabled = !loading.value && validate,
            isLoading = loading.value,
            onClick = { handleSubmit(email.text.toString(), password.text.toString()) },
            modifier = Modifier.fillMaxWidth().padding(
                top = 16.dp,
                bottom = 24.dp
            ).padding(horizontal = 24.dp)
        ) {
            Text(
                text = stringResource(R.string.login_text),
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Bold
                )
            )
        }
    }
    DisposableEffect(email) { onDispose { handleReset?.invoke() } }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun PreviewLoginScreen() {
    PeerTheme {
        val isLoading = remember { mutableStateOf<Boolean>(false) }
        val error = remember { mutableStateOf<String?>(null) }
        LoginScreen(
            loading = isLoading,
            error = error,
            onReset = {},
            onForgotPassword = {},
        ) { email, password -> }
    }
}
