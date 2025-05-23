package eu.peernetwork.user.ui.password.request

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import eu.peernetwork.core.ui.annotation.UiViewModel
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.compose.DesignButton
import eu.peernetwork.core.ui.design.compose.DesignTextField
import eu.peernetwork.core.ui.design.compose.DesignTitle
import eu.peernetwork.core.ui.design.compose.DesignTitleBarHost
import eu.peernetwork.core.ui.extension.builder
import eu.peernetwork.core.ui.extension.isValidEmail
import eu.peernetwork.core.ui.theme.PeerTheme
import eu.peernetwork.user.ui.R

@Composable
fun PasswordRequestScreen(
    email: String?,
    provider: UiComponentProvider,
    onFinish: () -> Unit,
) {
    val context = LocalContext.current
    val component = remember {
        provider.builder(PasswordRequest.Builder::class.java).build(context)
    }
    val viewModel = viewModel(
        modelClass = PasswordRequestViewModel::class.java,
        viewModelStoreOwner = UiViewModel.Owner(),
        factory = component.viewModelFactory()
    )
    val handleOnFinish by rememberUpdatedState(onFinish)
    val state by viewModel.state.collectAsStateWithLifecycle()
    val isLoading = remember(state) { derivedStateOf {
        state is PasswordRequestViewModel.State.Loading
    } }
    val isFinished = remember(state) { derivedStateOf { state is PasswordRequestViewModel.State.Success } }
    val error = remember(state) { derivedStateOf {
        (state as? PasswordRequestViewModel.State.Error?)?.error?.message?.let {
            component.resource().string(it)
        }
    } }
    PasswordRequestScreen(
        email,
        isLoading,
        error
    ) { viewModel.requestPassword(it) }
    DesignTitleBarHost("PasswordRequestScreen") {
        titleBar {
            DesignTitle {
                Text(stringResource(R.string.password_reset_label))
            }
        }
    }
    LaunchedEffect(isFinished.value) {
        if (isFinished.value) {
            handleOnFinish()
        }
    }
}

@Composable
fun PasswordRequestScreen(
    email: String?,
    loading: State<Boolean>,
    error: State<String?>,
    onSubmit: (String) -> Unit
) {
    var emailField = remember(email) { email?.let { TextFieldState(it) } ?: TextFieldState()  }
    val handleOnSubmit by rememberUpdatedState(onSubmit)
    Column(
        modifier = Modifier.fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
    ) {
        Text(
            stringResource(R.string.password_reset_header),
            style = MaterialTheme.typography.titleMedium.copy(
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.SemiBold
            )
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            stringResource(R.string.password_reset_instruction),
            style = MaterialTheme.typography.bodySmall.copy(
                color = MaterialTheme.colorScheme.tertiary
            )
        )
        DesignTextField(
            state = emailField,
            enabled = !loading.value,
            hasError = error.value != null,
            error = {
                error.value?.run {
                    Text(
                        text = this,
                        fontSize = MaterialTheme.typography.bodySmall.fontSize,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(horizontal = 16.dp)
                            .padding(top = 8.dp)
                    )
                }
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Send
            ),
            placeholder = { Text(stringResource(id = R.string.email_label)) },
            modifier = Modifier.padding(top = 12.dp)
        )
        DesignButton(
            isLoading = loading.value,
            enabled = !loading.value && emailField.isValidEmail(),
            onClick = {
                handleOnSubmit(emailField.text.toString())
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ) {
            Text(
                text = stringResource(R.string.confirmation_label),
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Bold
                )
            )
        }
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun PreviewPasswordRequestScreen() {
    PeerTheme {
        PasswordRequestScreen(
            "johnDoe@gmail.com",
            remember { mutableStateOf(false) },
            remember { mutableStateOf(null) },
        ) {}
    }
}
