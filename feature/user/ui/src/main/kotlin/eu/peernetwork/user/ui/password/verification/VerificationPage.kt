package eu.peernetwork.user.ui.password.verification

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.design.luna.DesignButton
import eu.peernetwork.core.ui.design.luna.DesignTextField
import eu.peernetwork.core.ui.extension.annotate
import eu.peernetwork.core.ui.theme.DesignTheme
import eu.peernetwork.feature.user.ui.R
import eu.peernetwork.user.ui.form.FormError
import eu.peernetwork.user.ui.form.FormHeader

@Composable
fun VerificationPage(
    email: String,
    isLoading: State<Boolean>,
    error: State<String?>,
    onVerified: (String) -> Unit
) {
    val token = remember { TextFieldState() }
    val isValidated = remember { derivedStateOf { token.text.isNotBlank() } }
    val handleOnVerified by rememberUpdatedState(onVerified)
    Column(
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
    ) {
        FormHeader(
            title = stringResource(R.string.forgot_password_text).annotate(),
            description = stringResource(R.string.password_verification_instruction, email),
            modifier = Modifier.padding(bottom = 32.dp)
        )
        DesignTextField(
            state = token,
            enabled = !isLoading.value,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            hint = stringResource(id = R.string.password_verification_label),
            minLines = 1,
            leading = {
                Icon(
                    painter = painterResource(R.drawable.ic_token),
                    contentDescription = stringResource(id = R.string.password_verification_label),
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .size(22.dp),
                    tint = LocalContentColor.current
                )
            },
            modifier = Modifier.padding(bottom = 6.dp)
        )
        FormError(
            error = error,
            modifier = Modifier.padding(horizontal = 18.dp)
        )
        DesignButton(
            onClick = { handleOnVerified(token.text.toString()) },
            enabled = !isLoading.value && isValidated.value,
            isLoading = isLoading.value,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 6.dp),
        ) { Text(stringResource(R.string.verify_text)) }
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun PreviewVerificationPage() {
    DesignTheme {
        val isLoading = remember { mutableStateOf(false) }
        val error = remember { mutableStateOf(null) }
        VerificationPage(
            email = "johnDoe@domain.com",
            isLoading = isLoading,
            error = error,
        ) {}
    }
}
