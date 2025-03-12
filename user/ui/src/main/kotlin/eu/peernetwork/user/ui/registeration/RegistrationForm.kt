package eu.peernetwork.user.ui.registeration

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
fun RegistrationForm(
    email: TextFieldState,
    username: TextFieldState,
    password: TextFieldState,
    modifier: Modifier = Modifier,
) {
    ConstraintLayout(modifier = modifier) {
        val (emailTag, usernameTag, passwordTag) = createRefs()
        DesignTextField(
            state = email,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            placeholder = { Text(stringResource(id = R.string.email_label)) },
            modifier = Modifier.constrainAs(emailTag) {
                top.linkTo(parent.top, margin = 8.dp)
                start.linkTo(parent.start, margin = 24.dp)
                end.linkTo(parent.end, margin = 24.dp)
                bottom.linkTo(usernameTag.top)
                width = Dimension.fillToConstraints
            }
        )
        DesignTextField(
            state = username,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            placeholder = { Text(stringResource(id = R.string.username_label)) },
            modifier = Modifier.constrainAs(usernameTag) {
                top.linkTo(emailTag.bottom, margin = 12.dp)
                start.linkTo(parent.start, margin = 24.dp)
                end.linkTo(parent.end, margin = 24.dp)
                bottom.linkTo(passwordTag.top)
                width = Dimension.fillToConstraints
            }
        )
        DesignTextField(
            state = password,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            textObfuscationMode = TextObfuscationMode.Hidden,
            placeholder = { Text(stringResource(id = R.string.password_label)) },
            modifier = Modifier.constrainAs(passwordTag) {
                top.linkTo(usernameTag.bottom, margin = 12.dp)
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
