package eu.peernetwork.user.ui.login

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.TextObfuscationMode
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.design.compose.DesignLabel
import eu.peernetwork.core.ui.design.compose.DesignSecureTextField
import eu.peernetwork.core.ui.design.compose.DesignTextField
import eu.peernetwork.core.ui.theme.PeerTheme
import eu.peernetwork.user.ui.R

@Composable
internal fun LoginForm(
    email: TextFieldState,
    password: TextFieldState,
    modifier: Modifier = Modifier,
    error: String? = null,
    enabled: Boolean,
    onForgotPassword: (String) -> Unit,
) {
    val handleOnForgotPassword by rememberUpdatedState(onForgotPassword)
    Column (modifier = modifier) {
        DesignTextField(
            state = email,
            enabled = enabled,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            placeholder = { Text(stringResource(id = R.string.email_label)) },
            modifier = Modifier.padding(horizontal = 24.dp)
        )
        DesignLabel(
            label = {
                Box {
                    Text(
                        text = stringResource(R.string.forgot_password_label),
                        modifier = Modifier.padding(horizontal = 24.dp)
                            .padding(horizontal = 16.dp)
                            .padding(top = 8.dp)
                            .clickable { handleOnForgotPassword(email.text.toString()) }
                    )
                }
            },
            visible = true,
            textStyle = MaterialTheme.typography.bodySmall.copy(
                color = MaterialTheme.colorScheme.tertiary
            )
        ) {
            DesignSecureTextField(
                state = password,
                enabled = enabled,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                showLabel = error != null,
                label = { error?.run {
                    Text(
                        text = this,
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .padding(top = 8.dp)
                    )
                } },
                placeholder = { Text(stringResource(id = R.string.password_label)) },
                textObfuscationMode = TextObfuscationMode.Hidden,
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .padding(top = 12.dp)
            )
        }
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun PreviewLoginForm() {
    val email = remember { TextFieldState("johnDoe@domain.com") }
    val password = remember { TextFieldState("*********") }
    PeerTheme {
        LoginForm(
            email = email,
            password = password,
            modifier = Modifier.fillMaxWidth(),
            enabled = true
        ) {}
    }
}
