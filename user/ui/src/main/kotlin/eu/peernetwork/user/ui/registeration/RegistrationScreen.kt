package eu.peernetwork.user.ui.registeration

import android.content.res.Configuration
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
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.compose.DesignButton
import eu.peernetwork.core.ui.compose.DesignPasswordStrength
import eu.peernetwork.core.ui.extension.builder
import eu.peernetwork.user.ui.R
import eu.peernetwork.core.ui.extension.isValidEmail
import eu.peernetwork.core.ui.extension.isValidInput
import eu.peernetwork.core.ui.extension.passwordStrength

@Composable
fun RegistrationScreen(
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    onRegistered: () -> Unit
) {
    val context = LocalContext.current
    val component = remember {
        provider.builder(Registration.Builder::class.java).build(context)
    }
    val viewModel = viewModel(
        modelClass = RegistrationViewModel::class.java,
        viewModelStoreOwner = viewModelStoreOwner,
        factory = component.viewModelFactory()
    )
    val state by viewModel.state.collectAsStateWithLifecycle()
    val isRegistered by remember(state) {
        derivedStateOf {
            state is RegistrationViewModel.State.Success
        }
    }
    RegistrationScaffold(
        loading = state is RegistrationViewModel.State.Loading,
        error = (state as? RegistrationViewModel.State.Error?)?.error?.message,
        onReset = { viewModel.reset() }
    ) { email, username, password ->
        viewModel.register(username, email, password)
    }
    LaunchedEffect(isRegistered) {
        if (isRegistered) {
            onRegistered()
        }
    }
}

@Composable
private fun RegistrationScaffold(
    loading: Boolean = false,
    error: String? = null,
    onReset: (() -> Unit)? = null,
    onSubmit: (String, String, String) -> Unit
) {
    val email = remember { TextFieldState() }
    val username = remember { TextFieldState() }
    var password = remember { TextFieldState() }
    val loadingState = rememberUpdatedState(loading)
    val errorState = rememberUpdatedState(error)
    val imeHeight = WindowInsets.ime.getBottom(LocalDensity.current) / 4
    val validate by remember(email, username, password) { derivedStateOf {
        email.isValidEmail() && username.isValidInput() &&
                (password.passwordStrength().value >= DesignPasswordStrength.STRONG.value)
    } }
    ConstraintLayout(modifier = Modifier.fillMaxWidth().padding(bottom = imeHeight.dp)) {
        val (form, cta) = createRefs()
        RegistrationForm(
            email = email,
            username = username,
            password = password,
            error = errorState.value,
            enabled = !loadingState.value,
            modifier = Modifier.constrainAs(form) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                width = Dimension.fillToConstraints
            }
        )
        DesignButton(
            enabled = !loadingState.value && validate,
            isLoading = loadingState.value,
            onClick = { onSubmit(
                email.text.toString(),
                username.text.toString(),
                password.text.toString()) },
            modifier = Modifier.constrainAs(cta) {
                top.linkTo(form.bottom, margin = 16.dp)
                start.linkTo(parent.start, margin = 24.dp)
                end.linkTo(parent.end, margin = 24.dp)
                bottom.linkTo(parent.bottom, margin = 24.dp)
                width = Dimension.fillToConstraints
            }
        ) {
            Text(
                text = stringResource(R.string.register_text),
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
fun PreviewRegistrationScaffold() {
    RegistrationScaffold { email, username, password -> }
}
