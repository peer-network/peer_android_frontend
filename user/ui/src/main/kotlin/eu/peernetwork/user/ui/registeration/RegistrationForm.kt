package eu.peernetwork.user.ui.registeration

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.TextObfuscationMode
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.compose.DesignSecureTextField
import eu.peernetwork.core.ui.compose.DesignTextField
import eu.peernetwork.core.ui.theme.PeerTheme
import eu.peernetwork.user.ui.R

@Composable
fun RegistrationForm(
    email: TextFieldState,
    username: TextFieldState,
    password: TextFieldState,
    modifier: Modifier = Modifier,
    error: String? = null,
) {
    Column (modifier = modifier) {
        DesignTextField(
            state = email,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            placeholder = { Text(stringResource(id = R.string.email_label)) },
            modifier = Modifier.padding(horizontal = 24.dp)
        )
        DesignTextField(
            state = username,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            placeholder = { Text(stringResource(id = R.string.username_label)) },
            modifier = Modifier.padding(horizontal = 24.dp)
                .padding(top = 12.dp)
        )
        DesignSecureTextField(
            state = password,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            hasError = error != null,
            error = { error?.run {
                Text(
                    text = this,
                    modifier = Modifier.padding(horizontal = 16.dp)
                        .padding(top = 6.dp)
                )
            } },
            textObfuscationMode = TextObfuscationMode.Hidden,
            placeholder = { Text(stringResource(id = R.string.password_label)) },
            modifier = Modifier.padding(horizontal = 24.dp)
                .padding(top = 12.dp)
        )
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun PreviewRegistrationForm() {
    val email = remember { TextFieldState("johnDoe@domain.com") }
    var username = remember { TextFieldState("johnDoe") }
    var password = remember { TextFieldState("*********") }
    PeerTheme {
        RegistrationForm(
            email = email,
            username = username,
            password = password,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
