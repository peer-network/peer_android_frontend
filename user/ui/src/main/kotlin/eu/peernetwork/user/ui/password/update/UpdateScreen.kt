package eu.peernetwork.user.ui.password.update

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
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
import eu.peernetwork.core.ui.design.luna.DesignButton
import eu.peernetwork.core.ui.design.material.DesignIndicatorColors
import eu.peernetwork.core.ui.design.material.DesignPasswordIndicator
import eu.peernetwork.core.ui.design.material.DesignPasswordStrength
import eu.peernetwork.core.ui.design.material.DesignTitle
import eu.peernetwork.core.ui.design.material.DesignTitleBarHost
import eu.peernetwork.core.ui.extension.builder
import eu.peernetwork.core.ui.extension.isValidInput
import eu.peernetwork.core.ui.extension.passwordStrength
import eu.peernetwork.core.ui.theme.DesignTheme
import eu.peernetwork.core.ui.theme.PeerAppGreen
import eu.peernetwork.core.ui.theme.PeerAppLightGreen
import eu.peernetwork.core.ui.theme.PeerAppRed
import eu.peernetwork.core.ui.theme.PeerAppYellow
import eu.peernetwork.user.ui.R
import eu.peernetwork.user.ui.form.FormError
import eu.peernetwork.user.ui.form.FormPassword

@Composable
fun PasswordUpdateScreen(
    provider: UiComponentProvider,
    onFinish: () -> Unit,
) {
    val context = LocalContext.current
    val component = remember {
        provider.builder(Update.Builder::class.java).build(context)
    }
    val viewModel = viewModel(
        modelClass = UpdateViewModel::class.java,
        viewModelStoreOwner = UiViewModel.Owner(),
        factory = component.viewModelFactory()
    )
    val handleOnFinish by rememberUpdatedState(onFinish)
    val state by viewModel.state.collectAsStateWithLifecycle()
    val isLoading = remember(state) { derivedStateOf {
        state is UpdateViewModel.State.Loading
    } }
    val isFinished = remember(state) { derivedStateOf { state is UpdateViewModel.State.Success } }
    val error = remember(state) { derivedStateOf {
        (state as? UpdateViewModel.State.Error?)?.error?.message?.let {
            component.resource().string(it)
        }
    } }
    PasswordUpdateScreen(isLoading, error) { current, new ->
        viewModel.update(current, new)
    }
    DesignTitleBarHost("PasswordResetScreen") {
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
fun PasswordUpdateScreen(
    loading: State<Boolean>,
    error: State<String?>,
    onSubmit: (String, String) -> Unit
) {
    val currentPassword = remember { TextFieldState() }
    val password = remember { TextFieldState() }
    val validate by remember(currentPassword, password) {
        derivedStateOf {
            currentPassword.isValidInput() &&
                    password.passwordStrength().value >= DesignPasswordStrength.STRONG.value
        }
    }
    val handleOnSubmit by rememberUpdatedState(onSubmit)
    Column(
        modifier = Modifier.fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            stringResource(R.string.password_reset_header),
            style = MaterialTheme.typography.titleMedium.copy(
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.SemiBold
            ),
            modifier = Modifier.padding(horizontal = 8.dp)
        )
        FormPassword(
            state = currentPassword,
            enabled = !loading.value,
            hint = stringResource(id = R.string.current_password_label),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.padding(top = 12.dp)
        )
        FormPassword(
            state = password,
            enabled = !loading.value,
            hint = stringResource(id = R.string.password_update_label),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.padding(top = 12.dp)
        )
        DesignPasswordIndicator(
            state = password,
            space = 4.dp,
            width = 24.dp,
            colors = DesignIndicatorColors(
                bad = MaterialTheme.colorScheme.outline,
                weak = PeerAppRed,
                medium = PeerAppYellow,
                good = PeerAppYellow,
                strong = PeerAppLightGreen,
                excellent = PeerAppGreen,
            ),
            modifier = Modifier
                .padding(top = 10.dp)
                .padding(horizontal = 18.dp)
                .height(2.dp)
        )
        FormError(
            error = error,
            modifier = Modifier.padding(horizontal = 18.dp)
                .padding(top = 8.dp)
        )
        DesignButton(
            isLoading = loading.value,
            enabled = !loading.value && validate,
            onClick = {
                handleOnSubmit(currentPassword.text.toString(), password.text.toString())
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 18.dp)
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
fun PreviewPasswordUpdateScreen() {
    DesignTheme {
        PasswordUpdateScreen(
            remember { mutableStateOf(false) },
            remember { mutableStateOf(null) },
        ) { token, password -> }
    }
}
