package eu.peernetwork.user.ui.registeration

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.compose.DesignPassword
import eu.peernetwork.core.ui.compose.DesignPasswordIndicator
import eu.peernetwork.core.ui.compose.DesignTextField
import eu.peernetwork.core.ui.theme.PeerTheme
import eu.peernetwork.user.ui.R

@Composable
fun RegistrationForm(
    email: TextFieldState,
    username: TextFieldState,
    password: TextFieldState,
    enabled: Boolean,
    modifier: Modifier = Modifier,
    error: String? = null,
) {
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
        DesignTextField(
            state = username,
            enabled = enabled,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            placeholder = { Text(stringResource(id = R.string.username_label)) },
            modifier = Modifier.padding(horizontal = 24.dp)
                .padding(top = 12.dp)
        )
        DesignPassword(
            state = password,
            enabled = enabled,
            showLabel = error != null,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            label = {
                error?.run {
                    Text(
                        text = this,
                        fontSize = MaterialTheme.typography.bodySmall.fontSize,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(horizontal = 40.dp)
                            .padding(top = 16.dp)
                    )
                }
            },
            indicator = {
                DesignPasswordIndicator(
                    state = password,
                    space = 16.dp,
                    width = 28.dp,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .padding(top = 12.dp).height(4.dp)
                )
            },
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
            modifier = Modifier.fillMaxWidth(),
            enabled = true
        )
    }
}
