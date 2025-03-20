package eu.peernetwork.user.ui.login

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.compose.DesignButton
import eu.peernetwork.core.ui.extension.builder
import eu.peernetwork.core.ui.theme.PeerTheme
import eu.peernetwork.user.ui.R
import eu.peernetwork.core.ui.extension.isValidEmail
import eu.peernetwork.core.ui.extension.isValidInput

@Composable
fun LoginScreen(
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner
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
    LoginContent(
        loading = state is LoginViewModel.State.Loading,
        error = (state as? LoginViewModel.State.Error?)?.error?.message,
        onReset = { viewModel.reset() }
    ) { email, password ->
        viewModel.login(email, password)
    }
}

@Composable
fun LoginContent(
    loading: Boolean = false,
    error: String? = null,
    onReset: (() -> Unit)? = null,
    onSubmit: (String, String) -> Unit
) {
    val email = remember { TextFieldState() }
    val password = remember { TextFieldState() }
    val loadingState = rememberUpdatedState(loading)
    val errorState = rememberUpdatedState(error)
    val validate by remember(email, password) { derivedStateOf {
        email.isValidEmail() && password.isValidInput()
    } }
    val imeHeight = WindowInsets.ime.getBottom(LocalDensity.current) / 4
    Column(
        modifier = Modifier.fillMaxWidth()
            .padding(bottom = imeHeight.dp)
    ) {
        LoginForm(
            email = email,
            password = password,
            error = errorState.value,
            enabled = !loadingState.value
        )
        DesignButton(
            enabled = !loadingState.value && validate,
            isLoading = loading,
            onClick = { onSubmit(email.text.toString(), password.text.toString()) },
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
    DisposableEffect(email) { onDispose { onReset?.invoke() } }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun PreviewLoginContent() {
    PeerTheme {
        LoginContent(
            onReset = {}
        ) { email, password -> }
    }
}
