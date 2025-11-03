package eu.peernetwork.user.ui.account

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.TextObfuscationMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.design.luna.DesignButton
import eu.peernetwork.core.ui.design.luna.DesignTextField
import eu.peernetwork.core.ui.design.material.DesignBottomSheetScaffold
import eu.peernetwork.core.ui.design.material.DesignSecureTextField
import eu.peernetwork.core.ui.extension.isValidInput
import eu.peernetwork.core.ui.theme.DesignTheme
import eu.peernetwork.user.ui.R
import eu.peernetwork.user.ui.form.ErrorLabel

@Composable
fun AccountForm(
    username: TextFieldState,
    bio: TextFieldState,
    enable: State<Boolean>,
    isLoading: State<Boolean>,
    showPassword: MutableState<Boolean>,
    error: State<String?>,
    maxText: Int = 500,
    onVerify: (String) -> Unit = {},
    onSubmit: () -> Unit
) {
    val password = remember { TextFieldState() }
    val handleVerify by rememberUpdatedState(onVerify)
    val action = remember { mutableStateOf<(() -> Unit)?>(null) }
    val focus = remember { FocusRequester() }
    val isValidLength = remember {
        derivedStateOf {
            bio.text.length <= maxText
        }
    }
    DesignTextField(
        state = username,
        enabled = !isLoading.value,
        hint = stringResource(R.string.username_label)
    )
    Box(
        contentAlignment = Alignment.BottomEnd,
        modifier = Modifier.padding(
            top = 12.dp,
            bottom = 8.dp,
        )
    ) {
        DesignTextField(
            state = bio,
            contentPadding = PaddingValues(
                top = 16.dp,
                start = 16.dp,
                end = 16.dp,
                bottom = 56.dp,
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            enabled = !isLoading.value,
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier.fillMaxWidth(),
            leading = {
                Text(
                    text = stringResource(R.string.description_label),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier
                        .padding(end = 10.dp)
                        .align(Alignment.Top)
                )
            },
            hint = stringResource(R.string.description_placeholder)
        )
        Text(
            text = "${bio.text.length}/$maxText",
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.bodySmall.copy(
                color = if (isValidLength.value) {
                    MaterialTheme.colorScheme.outlineVariant
                } else {
                    MaterialTheme.colorScheme.error
                }
            )
        )
    }
    ErrorLabel(
        error = error,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp)
    )
    DesignButton(
        onClick = onSubmit,
        enabled = !isLoading.value && enable.value,
        isLoading = isLoading.value,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
    ) { Text(stringResource(R.string.save_text)) }
    DesignBottomSheetScaffold(
        state = showPassword,
        color = MaterialTheme.colorScheme.surfaceContainerLow,
        onShow = { focus.requestFocus() },
        onDismiss = {
            action.value?.invoke()
            action.value = null
            showPassword.value = false
        }
    ) {
        AccountPassword(
            state = showPassword,
            password = password,
            label = stringResource(R.string.confirmation_label),
            focus = focus,
            onSubmit = {
                action.value = { handleVerify(it) }
                showPassword.value = false
            }
        )
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
private fun AccountPassword(
    state: MutableState<Boolean>,
    password: TextFieldState,
    label: String,
    focus: FocusRequester,
    onSubmit: (String) -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp)
    ) {
        DesignSecureTextField(
            state = password,
            focusRequester = focus,
            enabled = state.value,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            placeholder = { Text(stringResource(id = R.string.password_label)) },
            textObfuscationMode = TextObfuscationMode.Hidden,
        )
        Spacer(modifier = Modifier.height(16.dp))
        DesignButton(
            enabled = state.value && password.isValidInput(),
            onClick = { onSubmit(password.text.toString()) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Bold
                )
            )
        }
        Spacer(modifier = Modifier.navigationBarsPadding())
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun PreviewSettingsForm() {
    DesignTheme {
        val state = remember { mutableStateOf(true) }
        val username = remember { TextFieldState() }
        val bio = remember { TextFieldState() }
        Column(modifier = Modifier.padding(24.dp)) {
            AccountForm(
                username,
                bio,
                remember { mutableStateOf(false) },
                remember { mutableStateOf(false) },
                state,
                remember { mutableStateOf("Hello, world!") },
            ) {}
        }
    }
}
