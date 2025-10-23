package eu.peernetwork.user.ui.password.request

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.ClickableText
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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import eu.peernetwork.core.ui.annotation.UiViewModel
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.material.DesignButton
import eu.peernetwork.core.ui.design.material.DesignTextField
import eu.peernetwork.core.ui.design.material.DesignTitle
import eu.peernetwork.core.ui.design.material.DesignTitleBarHost
import eu.peernetwork.core.ui.extension.builder
import eu.peernetwork.core.ui.extension.isValidEmail
import eu.peernetwork.core.ui.extension.navigateIfNecessary
import eu.peernetwork.core.ui.theme.PeerTheme
import eu.peernetwork.user.ui.R
import eu.peernetwork.user.ui.v2.password.request.RequestViewModel

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
        modelClass = RequestViewModel::class.java,
        viewModelStoreOwner = UiViewModel.Owner(),
        factory = component.viewModelFactory()
    )
    val state by viewModel.state.collectAsStateWithLifecycle()
    val isLoading = remember(state) { derivedStateOf {
        state is RequestViewModel.State.Loading
    } }
    val isFinished = remember(state) { derivedStateOf { state is RequestViewModel.State.Success } }
    val error = remember(state) { derivedStateOf {
        (state as? RequestViewModel.State.Error?)?.error?.message?.let {
            component.resource().string(it)
        }
    } }
    val controller = rememberNavController()
    PasswordRequestNavigation(
        controller,
        component,
        onFinish
    ) {
        PasswordRequestScreen(
            email,
            isLoading,
            error,
            { controller.navigateIfNecessary("passwordReset") }
        ) { viewModel.requestPassword(it) }
        DesignTitleBarHost("PasswordRequestScreen", onFinish) {
            titleBar {
                DesignTitle {
                    Text(stringResource(R.string.password_recovery_label))
                }
            }
        }
    }
    LaunchedEffect(isFinished.value) {
        if (isFinished.value) {
            controller.navigateIfNecessary("passwordReset")
        }
    }
}

@Composable
fun PasswordRequestScreen(
    email: String?,
    loading: State<Boolean>,
    error: State<String?>,
    onFinish: () -> Unit,
    onSubmit: (String) -> Unit
) {
    var emailField = remember(email) { email?.let { TextFieldState(it) } ?: TextFieldState()  }
    val handleOnFinish by rememberUpdatedState(onFinish)
    val handleOnSubmit by rememberUpdatedState(onSubmit)
    val annotatedText = buildAnnotatedString {
        append(stringResource(R.string.password_reset_instruction))
        append(" ")
        append(stringResource(R.string.password_alternative_question))
        pushStringAnnotation(tag = "TOKEN", annotation = "token")
        withStyle(style = SpanStyle(
            color = MaterialTheme.colorScheme.tertiary,
            fontWeight = FontWeight.SemiBold,
            textDecoration = TextDecoration.Underline
        )) {
            append(" ")
            append(stringResource(R.string.click_here).lowercase())
        }
        pop()
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 16.dp, horizontal = 24.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            stringResource(R.string.password_reset_header),
            style = MaterialTheme.typography.titleMedium.copy(
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.SemiBold
            ),
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        ClickableText(
            annotatedText,
            style = MaterialTheme.typography.bodySmall.copy(
                color = MaterialTheme.colorScheme.tertiary
            ),
            modifier = Modifier.padding(horizontal = 16.dp),
            onClick = { offset ->
                annotatedText.getStringAnnotations(tag = "TOKEN", start = offset, end = offset)
                    .firstOrNull()?.let {
                        handleOnFinish()
                    }
            }
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
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .padding(top = 6.dp)
                    )
                }
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Send
            ),
            placeholder = { Text(stringResource(id = R.string.email_label)) },
            modifier = Modifier.padding(top = 16.dp)
        )
        DesignButton(
            isLoading = loading.value,
            enabled = !loading.value && emailField.isValidEmail(),
            onClick = {
                handleOnSubmit(emailField.text.toString())
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp)
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
            {}
        ) {}
    }
}
