package eu.peernetwork.user.ui.registeration

import android.content.res.Configuration
import android.widget.Toast
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
import androidx.compose.runtime.LaunchedEffect
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
import eu.peernetwork.core.ui.design.compose.DesignButton
import eu.peernetwork.core.ui.design.compose.DesignPasswordStrength
import eu.peernetwork.core.ui.extension.builder
import eu.peernetwork.user.ui.R
import eu.peernetwork.core.ui.extension.isValidEmail
import eu.peernetwork.core.ui.extension.isValidInput
import eu.peernetwork.core.ui.extension.passwordStrength
import eu.peernetwork.core.ui.theme.PeerTheme

@Composable
fun RegistrationScreen(
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    onRegistrationSuccess: () -> Unit
) {
    val context = LocalContext.current
    val successMessage = stringResource(R.string.successful_message)
    val component = remember {
        provider.builder(Registration.Builder::class.java).build(context)
    }
    val handleRegistrationSuccess by rememberUpdatedState(onRegistrationSuccess)
    val viewModel = viewModel(
        modelClass = RegistrationViewModel::class.java,
        viewModelStoreOwner = viewModelStoreOwner,
        factory = component.viewModelFactory()
    )
    val state by viewModel.state.collectAsStateWithLifecycle()
    RegistrationScreen(
        loading = state is RegistrationViewModel.State.Loading,
        error = (state as? RegistrationViewModel.State.Error?)?.error?.message?.let {
            component.resource().string(it)
        },
        onReset = { viewModel.reset() }
    ) { email, username, password ->
        viewModel.register(username, email, password)
    }
    LaunchedEffect(state) {
        if (state is RegistrationViewModel.State.Success) {
            Toast.makeText(context, successMessage, Toast.LENGTH_SHORT).show()
            handleRegistrationSuccess()
        }
    }
}

@Composable
fun RegistrationScreen(
    loading: Boolean = false,
    error: String? = null,
    onReset: (() -> Unit)? = null,
    onSubmit: (String, String, String) -> Unit
) {
    val email = remember { TextFieldState() }
    val username = remember { TextFieldState() }
    val referralCode = remember { TextFieldState() }
    val password = remember { TextFieldState() }

    val loadingState = rememberUpdatedState(loading)
    val errorState = rememberUpdatedState(error)
    val imeHeight = WindowInsets.ime.getBottom(LocalDensity.current) / 4
    val validate by remember(email, username, password) {
        derivedStateOf {
            email.isValidEmail() &&
                    username.isValidInput() &&
                    password.passwordStrength().value >= DesignPasswordStrength.STRONG.value
        }
    }
    val handleReset by rememberUpdatedState(onReset)
    val handleSubmit by rememberUpdatedState(onSubmit)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = imeHeight.dp)
    ) {
        RegistrationForm(
            email = email,
            username = username,
            password = password,
            referralCode = referralCode,
            error = errorState.value,
            enabled = !loadingState.value,
        )
        DesignButton(
            enabled = !loadingState.value && validate,
            isLoading = loadingState.value,
            onClick = {
                handleSubmit(
                    email.text.toString(),
                    username.text.toString(),
                    password.text.toString()
                    // No referralCode in submission logic yet
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 24.dp)
                .padding(horizontal = 24.dp)
        ) {
            Text(
                text = stringResource(R.string.register_text),
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
fun PreviewRegistrationScreen() {
    PeerTheme {
        RegistrationScreen { email, username, password -> }
    }
}
