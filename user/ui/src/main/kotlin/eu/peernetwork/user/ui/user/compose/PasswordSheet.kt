package eu.peernetwork.user.ui.user.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.TextObfuscationMode
import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.compose.DesignBottomSheet
import eu.peernetwork.core.ui.compose.DesignButton
import eu.peernetwork.core.ui.compose.DesignOverlay
import eu.peernetwork.core.ui.compose.DesignOverlayBackground
import eu.peernetwork.core.ui.compose.DesignSecureTextField
import eu.peernetwork.core.ui.extension.isValidInput
import eu.peernetwork.core.ui.theme.PeerTheme
import eu.peernetwork.user.ui.R
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun PasswordSheet(
    state: MutableState<Boolean>,
    initialValue: SheetValue = SheetValue.Hidden,
    onSubmit: (String) -> Unit = {}
) {
    val focus = remember { FocusRequester() }
    val password = remember { TextFieldState() }
    DesignBottomSheet(
        showSheet = state,
        tag = "passwordSheet",
        onDismissRequest = { state.value = false },
        initialValue = initialValue,
        color = MaterialTheme.colorScheme.surfaceVariant,
        background = {
            DesignOverlayBackground(
                state = state,
                modifier = Modifier.fillMaxSize()
                    .background(MaterialTheme.colorScheme.background.copy(alpha = .6f))
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
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
                    text = stringResource(R.string.confirmation_label),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
            }
            Spacer(modifier = Modifier.navigationBarsPadding())
        }
    }
    LaunchedEffect(state.value) {
        if (!state.value) {
            password.clearText()
        } else {
            delay(200)
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
        DesignOverlay {
            PasswordSheet(
                state = state,
                initialValue = SheetValue.Expanded
            )
        }
    }
}
