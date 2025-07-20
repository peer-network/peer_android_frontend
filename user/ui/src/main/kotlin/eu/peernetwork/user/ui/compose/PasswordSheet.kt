package eu.peernetwork.user.ui.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.TextObfuscationMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.design.compose.DesignBottomSheetScaffold
import eu.peernetwork.core.ui.design.compose.DesignButton
import eu.peernetwork.core.ui.design.compose.DesignSecureTextField
import eu.peernetwork.core.ui.extension.isValidInput
import eu.peernetwork.core.ui.theme.PeerTheme
import eu.peernetwork.user.ui.R

@Composable
fun PasswordSheet(
    state: MutableState<Boolean>,
    label: String,
    onSubmit: (String) -> Unit = {}
) {
    val password = remember { TextFieldState() }
    val handleSubmit by rememberUpdatedState(onSubmit)
    val action = remember { mutableStateOf<(() -> Unit)?>(null) }
    DesignBottomSheetScaffold(
        state = state,
        color = MaterialTheme.colorScheme.surfaceContainerLow,
        onDismiss = {
            action.value?.invoke()
            action.value = null
            state.value = false
        }
    ) {
        PasswordSheet(
            state = state,
            password = password,
            label = label,
            onSubmit = {
                action.value = { handleSubmit(it) }
                state.value = false
            }
        )
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
fun PasswordSheet(
    state: MutableState<Boolean>,
    password: TextFieldState,
    label: String,
    onSubmit: (String) -> Unit = {}
) {
    val focus = remember { FocusRequester() }
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
        LaunchedEffect(Unit) {
            focus.requestFocus()
        }
    }
}

@Composable
@Preview
@OptIn(ExperimentalMaterial3Api::class)
fun PreviewPasswordSheet() {
    PeerTheme {
        val state = remember { mutableStateOf(true) }
        val password = remember { TextFieldState() }
        PasswordSheet(
            state = state,
            password = password,
            label = stringResource(R.string.confirmation_label),
        )
    }
}
