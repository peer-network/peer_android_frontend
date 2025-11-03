package eu.peernetwork.user.ui.login

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.design.luna.DesignButton
import eu.peernetwork.core.ui.design.luna.DesignTextField
import eu.peernetwork.core.ui.design.luna.designSecondaryButtonColors
import eu.peernetwork.core.ui.extension.annotate
import eu.peernetwork.core.ui.extension.isValidEmail
import eu.peernetwork.core.ui.extension.isValidInput
import eu.peernetwork.core.ui.theme.DesignTheme
import eu.peernetwork.user.ui.R
import eu.peernetwork.user.ui.form.ErrorLabel
import eu.peernetwork.user.ui.form.LabelledCheckBox
import eu.peernetwork.user.ui.form.PasswordTextField

@Composable
fun LoginForm(
    email: TextFieldState,
    password: TextFieldState,
    isLoading: State<Boolean>,
    error: State<String?>,
    modifier: Modifier = Modifier,
    rememberMe: MutableState<Boolean>,
    onLogin: () -> Unit,
    onPasswordReset: () -> Unit,
    onRegister: () -> Unit
) {
    val isValidated = remember { derivedStateOf {
        email.isValidEmail() && password.isValidInput()
    } }
    Column(modifier = modifier) {
        DesignTextField(
            state = email,
            enabled = !isLoading.value,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            hint = stringResource(id = R.string.email_label),
            leading = {
                Icon(
                    painter = painterResource(R.drawable.ic_email),
                    contentDescription = stringResource(id = R.string.email_label),
                    modifier = Modifier.padding(end = 8.dp)
                        .size(22.dp),
                    tint = LocalContentColor.current
                )
            }
        )
        PasswordTextField(
            state = password,
            enabled = !isLoading.value,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            modifier = Modifier.padding(top = 12.dp)
        )
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
                .padding(top = 10.dp, bottom = 8.dp)
        ) {
            LabelledCheckBox(
                state = rememberMe,
                label = stringResource(R.string.remember_me).annotate(),
                modifier = Modifier.padding(horizontal = 18.dp)
            )
            Text(
                text = stringResource(R.string.forgot_password_label),
                color = MaterialTheme.colorScheme.scrim,
                style = MaterialTheme.typography.labelMedium,
                textDecoration = TextDecoration.Underline,
                modifier = Modifier.clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                    onClick = onPasswordReset
                )
            )
        }
        ErrorLabel(
            error = error,
            modifier = Modifier.padding(horizontal = 18.dp)
        )
        DesignButton(
            onClick = onLogin,
            enabled = !isLoading.value && isValidated.value,
            isLoading = isLoading.value,
            modifier = Modifier.fillMaxWidth()
                .padding(top = 10.dp),
        ) { Text(stringResource(R.string.login_text)) }
        Divider(modifier = Modifier.padding(top = 16.dp))
        DesignButton(
            onClick = onRegister,
            colors = designSecondaryButtonColors(),
            modifier = Modifier.fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Row {
                Text(stringResource(R.string.register_now_text))
                Icon(
                    painter = painterResource(R.drawable.ic_right),
                    contentDescription = null,
                    modifier = Modifier.padding(start = 8.dp)
                        .size(18.dp),
                    tint = LocalContentColor.current
                )
            }
        }
    }
}

@Composable
private fun Divider(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.weight(1f)
            .height(1.dp)
            .padding(end = 10.dp)
            .background(
                color = MaterialTheme.colorScheme.surfaceContainerLow,
                shape = CircleShape
            ))
        Text(
            text = stringResource(R.string.or),
            color = MaterialTheme.colorScheme.outlineVariant,
            style = MaterialTheme.typography.labelMedium
        )
        Spacer(modifier = Modifier.weight(1f)
            .height(1.dp)
            .padding(start = 10.dp)
            .background(
                color = MaterialTheme.colorScheme.surfaceContainerLow,
                shape = CircleShape
            ))
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun PreviewLoginForm() {
    DesignTheme {
        val email = remember { TextFieldState("johnDoe@domain.com") }
        val password = remember { TextFieldState("*********") }
        val isLoading = remember { mutableStateOf(false) }
        val error = remember { mutableStateOf(null) }
        val rememberMe = remember { mutableStateOf(true) }
        LoginForm(
            email = email,
            password = password,
            isLoading = isLoading,
            error = error,
            modifier = Modifier.padding(24.dp),
            rememberMe = rememberMe,
            onLogin = {},
            onPasswordReset = {},
            onRegister = {}
        )
    }
}
