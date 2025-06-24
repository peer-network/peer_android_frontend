package eu.peernetwork.user.ui.compose

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
import eu.peernetwork.core.ui.design.compose.DesignBottomSheet
import eu.peernetwork.core.ui.design.compose.DesignButton
import eu.peernetwork.core.ui.design.compose.DesignOverlay
import eu.peernetwork.core.ui.design.compose.DesignOverlayBackground
import eu.peernetwork.core.ui.design.compose.DesignSecureTextField
import eu.peernetwork.core.ui.extension.isValidInput
import eu.peernetwork.core.ui.theme.PeerTheme
import eu.peernetwork.user.ui.R

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun PasswordSheet(
    state: MutableState<Boolean>,
    label: String,
    initialValue: SheetValue = SheetValue.Hidden,
    onSubmit: (String) -> Unit = {}
) {
    val focus = remember { FocusRequester() }
    val password = remember { TextFieldState() }
    DesignBottomSheet(
        showSheet = state,
        tag = label,
        onAnimationComplete = {
            if (!it) {
                password.clearText()
            } else {
                focus.requestFocus()
            }
        },
        onDismissRequest = { state.value = false },
        initialValue = initialValue,
        color = MaterialTheme.colorScheme.surfaceContainerLow,
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
                    text = label,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
            }
            Spacer(modifier = Modifier.navigationBarsPadding())
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
                label = stringResource(R.string.confirmation_label),
                initialValue = SheetValue.Expanded
            )
        }
    }
}
