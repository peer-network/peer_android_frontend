package eu.peernetwork.user.ui.email

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
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
import androidx.compose.ui.res.painterResource
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
import eu.peernetwork.core.ui.design.luna.DesignTextField
import eu.peernetwork.core.ui.design.material.DesignTitle
import eu.peernetwork.core.ui.design.material.DesignTitleBarHost
import eu.peernetwork.core.ui.extension.builder
import eu.peernetwork.core.ui.extension.isValidEmail
import eu.peernetwork.core.ui.extension.isValidInput
import eu.peernetwork.core.ui.theme.DesignTheme
import eu.peernetwork.feature.user.ui.R
import eu.peernetwork.user.ui.form.FormError
import eu.peernetwork.user.ui.form.FormPassword

@Composable
fun EmailScreen(
    provider: UiComponentProvider,
    onFinish: () -> Unit,
) {
    val context = LocalContext.current
    val component = remember {
        provider.builder(Email.Builder::class.java).build(context)
    }
    val viewModel = viewModel(
        modelClass = EmailViewModel::class.java,
        viewModelStoreOwner = UiViewModel.Owner(),
        factory = component.viewModelFactory()
    )
    val handleOnFinish by rememberUpdatedState(onFinish)
    val state by viewModel.state.collectAsStateWithLifecycle()
    val isLoading = remember(state) { derivedStateOf {
        state is EmailViewModel.State.Loading
    } }
    val isFinished = remember(state) { derivedStateOf { state is EmailViewModel.State.Success } }
    val error = remember(state) { derivedStateOf {
        (state as? EmailViewModel.State.Error?)?.error?.message?.let {
            component.resource().string(it)
        }
    } }
    EmailScreen(isLoading, error) { email, password ->
        viewModel.update(email, password)
    }
    DesignTitleBarHost("PasswordResetScreen") {
        titleBar {
            DesignTitle {
                Text(stringResource(R.string.email_label))
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
fun EmailScreen(
    loading: State<Boolean>,
    error: State<String?>,
    onSubmit: (String, String) -> Unit
) {
    val email = remember { TextFieldState() }
    val password = remember { TextFieldState() }
    val handleOnSubmit by rememberUpdatedState(onSubmit)
    val validate by remember(email, password) {
        derivedStateOf {
            email.isValidEmail() && password.isValidInput()
        }
    }
    Column(modifier = Modifier.fillMaxSize()
        .padding(horizontal = 16.dp, vertical = 16.dp)
        .verticalScroll(rememberScrollState())
    ) {
        Text(
            stringResource(R.string.email_update_label),
            style = MaterialTheme.typography.titleMedium.copy(
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.SemiBold
            ),
            modifier = Modifier.padding(horizontal = 8.dp)
        )
        DesignTextField(
            state = email,
            enabled = !loading.value,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            hint = stringResource(id = R.string.email_label),
            leading = {
                Icon(
                    painter = painterResource(R.drawable.ic_email),
                    contentDescription = stringResource(id = R.string.email_label),
                    modifier = Modifier.padding(end = 8.dp)
                        .size(22.dp),
                    tint = LocalContentColor.current
                )
            },
            modifier = Modifier.padding(top = 16.dp)
        )
        FormPassword(
            state = password,
            enabled = !loading.value,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            modifier = Modifier.padding(top = 12.dp)
                .padding(bottom = 8.dp)
        )
        FormError(
            error = error,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp)
        )
        DesignButton(
            isLoading = loading.value,
            enabled = !loading.value && validate,
            onClick = {
                handleOnSubmit(email.text.toString(), password.text.toString())
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
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
fun PreviewAddressScreen() {
    DesignTheme {
        EmailScreen(
            remember { mutableStateOf(false) },
            remember { mutableStateOf(null) },
        ) { token, password -> }
    }
}
