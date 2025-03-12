package eu.peernetwork.user.ui.login

import android.content.res.Configuration
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import eu.peernetwork.core.ui.compose.DesignTextField
import eu.peernetwork.core.ui.theme.PeerTheme
import eu.peernetwork.user.ui.R

@Composable
internal fun LoginForm(
    email: TextFieldState,
    password: TextFieldState,
    modifier: Modifier = Modifier,
) {
    ConstraintLayout(modifier = modifier) {
        val (emailText, passwordText) = createRefs()
        DesignTextField(
            state = email,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            placeholder = { Text(stringResource(id = R.string.email_label)) },
            modifier = Modifier.constrainAs(emailText) {
                top.linkTo(parent.top, margin = 8.dp)
                start.linkTo(parent.start, margin = 24.dp)
                end.linkTo(parent.end, margin = 24.dp)
                bottom.linkTo(passwordText.top)
                width = Dimension.fillToConstraints
            }
        )
        DesignTextField(
            state = password,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            placeholder = { Text(stringResource(id = R.string.password_label)) },
            textObfuscationMode = TextObfuscationMode.Hidden,
            modifier = Modifier.constrainAs(passwordText) {
                top.linkTo(emailText.bottom, margin = 12.dp)
                start.linkTo(parent.start, margin = 24.dp)
                end.linkTo(parent.end, margin = 24.dp)
                bottom.linkTo(parent.bottom, margin = 8.dp)
                width = Dimension.fillToConstraints
            }
        )
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
            modifier = Modifier.fillMaxWidth()
        )
    }
}
