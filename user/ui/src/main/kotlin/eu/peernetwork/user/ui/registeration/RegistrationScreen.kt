package eu.peernetwork.user.ui.registeration

import androidx.compose.runtime.saveable.rememberSaveable
import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.AlertDialog
//import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
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
    referral: String?,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    onRegistrationSuccess: () -> Unit
) {
    val context = LocalContext.current
    val successMessage = stringResource(R.string.successful_message)
    val component = remember {
        provider.builder(Registration.Builder::class.java).build(context)
    }
    val viewModel = viewModel(
        modelClass = RegistrationViewModel::class.java,
        viewModelStoreOwner = viewModelStoreOwner,
        factory = component.viewModelFactory()
    )
    val state by viewModel.state.collectAsStateWithLifecycle()
    val handleRegistrationSuccess by rememberUpdatedState(onRegistrationSuccess)
    val loading = remember { derivedStateOf { state is RegistrationViewModel.State.Loading } }
    val error = remember { derivedStateOf {
        (state as? RegistrationViewModel.State.Error?)?.error?.message?.let {
            component.resource().string(it)
        }
    } }

    RegistrationScreen(
        referral = referral,
        loading = loading,
        error = error,
        onReset = { viewModel.reset() }
    ) { email, username, password, referral ->
        viewModel.register(username, email, password, referral)
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
    loading: State<Boolean>,
    error: State<String?>,
    referral: String? = null,
    onReset: (() -> Unit)? = null,
    onSubmit: (String, String, String, String) -> Unit
) {
    val email by rememberSaveable(stateSaver = TextFieldState.Saver) { mutableStateOf(TextFieldState()) }
    val username by rememberSaveable(stateSaver = TextFieldState.Saver) {
        mutableStateOf(
            TextFieldState()
        )
    }
    val referralCode by rememberSaveable(stateSaver = TextFieldState.Saver) {
        mutableStateOf(referral?.let { TextFieldState(it) } ?: TextFieldState())
    }
    var password = remember { TextFieldState() }

    val validate by remember(email, username, password) {
        derivedStateOf {
            email.isValidEmail() &&
                    username.isValidInput() &&
                    password.passwordStrength().value >= DesignPasswordStrength.STRONG.value
        }
    }

    val handleReset by rememberUpdatedState(onReset)
    val handleSubmit by rememberUpdatedState(onSubmit)

    // State to control showing age confirmation dialog
    val showAgeConfirmDialog = remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxWidth()) {
        RegistrationForm(
            email = email,
            username = username,
            password = password,
            referralCode = referralCode,
            error = error.value,
            enabled = !loading.value,
        )

        DesignButton(
            enabled = !loading.value && validate,
            isLoading = loading.value,
            onClick = {
                // Show age confirmation popup on register button click?
                showAgeConfirmDialog.value = true
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

    // Age confirmation popup dialog appears
    if (showAgeConfirmDialog.value) {
        var isChecked by remember { mutableStateOf(false) }
        AlertDialog(
            onDismissRequest = { /* Prevent dismiss by clicking outside or back button */ },
            title = { Text(text = stringResource(id = R.string.age_confirmation_title)) },
            text = {
                Column {
                    Text(text = stringResource(id = R.string.age_confirmation_message))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(top = 8.dp)
                    ) {
                        Checkbox(
                            checked = isChecked,
                            onCheckedChange = { isChecked = it }
                        )
                        Text(text = stringResource(id = R.string.age_confirmation_checkbox))
                    }
                }
            },
            confirmButton = {
                DesignButton(
                    enabled = isChecked,
                    onClick = {
                        showAgeConfirmDialog.value = false
                        // Proceed with registration after age confirmation
                        handleSubmit(
                            email.text.toString(),
                            username.text.toString(),
                            password.text.toString(),
                            referralCode.text.toString()
                        )
                    }
                ) {
                    Text(text = stringResource(id = R.string.age_confirmation_continue))
                }
            }
        )
    }

    DisposableEffect(email) { onDispose { handleReset?.invoke() } }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun PreviewRegistrationScreen() {
    PeerTheme {
        val isLoading = remember { mutableStateOf(false) }
        val error = remember { mutableStateOf<String?>(null) }
        RegistrationScreen(isLoading, error) { email, username, password, referral -> }
    }
}
