package eu.peernetwork.user.ui.deactivate

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.TextObfuscationMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import eu.peernetwork.core.ui.design.luna.DesignOutlineButton
import eu.peernetwork.core.ui.design.luna.DesignPassword
import eu.peernetwork.core.ui.design.luna.designSecondaryButtonColors
import eu.peernetwork.core.ui.extension.isValidInput
import eu.peernetwork.core.ui.theme.DesignTheme
import eu.peernetwork.feature.user.ui.R


@Composable
@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
fun DeactivateSheet(
    state: MutableState<Boolean>,
    isLoading: State<Boolean>,
    password: TextFieldState,
    focus: FocusRequester,
    onCancel: () -> Unit = {},
    onSubmit: (String) -> Unit = {}
) {
    val isValidated = remember { derivedStateOf {
        state.value && password.isValidInput() && !isLoading.value
    } }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(top = 16.dp)
            .padding(bottom = 12.dp)
            .navigationBarsPadding()
    ) {
        DeactivateTitle()
        DesignPassword(
            state = password,
            hint = stringResource(id = R.string.password_label),
            focusRequester = focus,
            enabled = state.value,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            contentPadding = PaddingValues(16.dp),
            textObfuscationMode = TextObfuscationMode.Hidden,
            modifier = Modifier.fillMaxWidth()
                .padding(top = 16.dp)
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.padding(top = 12.dp)
                .padding(bottom = 6.dp)
        ) {
            DesignOutlineButton(
                onClick = onCancel,
                minHeight = 42.dp,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                contentPadding = PaddingValues(horizontal = 12.dp),
                modifier = Modifier.weight(1f)
            ) { Text(stringResource(R.string.cancel_label)) }
            DesignButton(
                onClick = { onSubmit(password.text.toString()) },
                minHeight = 42.dp,
                enabled = isValidated.value,
                isLoading = isLoading.value,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                colors = designSecondaryButtonColors(),
                contentPadding = PaddingValues(horizontal = 12.dp),
                modifier = Modifier.weight(1f)
            ) { Text(stringResource(R.string.deactivate_text)) }
        }
    }
}

@Composable
@Preview
@OptIn(ExperimentalMaterial3Api::class)
fun PreviewLogoutSheet() {
    DesignTheme(isDarkMode = true) {
        val state = remember { mutableStateOf(false) }
        val isLoading = remember { mutableStateOf(false) }
        val password = remember { TextFieldState() }
        val focus = remember { FocusRequester() }
        DeactivateSheet(
            state = state,
            isLoading = isLoading,
            password = password,
            focus = focus
        ) {}
    }
}
