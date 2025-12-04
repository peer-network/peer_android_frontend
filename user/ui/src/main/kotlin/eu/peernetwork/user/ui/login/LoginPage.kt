package eu.peernetwork.user.ui.login

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.extension.annotate
import eu.peernetwork.core.ui.extension.isValidEmail
import eu.peernetwork.core.ui.theme.PeerTheme
import eu.peernetwork.user.ui.R
import eu.peernetwork.user.ui.form.FormHeader

@Composable
fun LoginPage(
    login: String?,
    isLoading: State<Boolean>,
    error: State<String?>,
    onLogin: (String, String, Boolean) -> Unit,
    onPasswordReset: (String) -> Unit,
    onPrivacy: () -> Unit,
    onRegister: () -> Unit
) {
    val email by rememberSaveable(stateSaver = TextFieldState.Saver) {
        mutableStateOf(TextFieldState(login ?: ""))
    }
    val password by rememberSaveable(stateSaver = TextFieldState.Saver) { mutableStateOf(TextFieldState()) }
    val rememberMe = rememberSaveable { mutableStateOf(true) }
    val isDarkTheme = MaterialTheme.colorScheme.background.luminance() < 0.5
    val handleOnLogin by rememberUpdatedState(onLogin)
    val handleOnPasswordReset by rememberUpdatedState(onPasswordReset)
    val handleOnPrivacy by rememberUpdatedState(onPrivacy)
    Column {
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 24.dp)
        ) {
            Image(
                painter = if (isDarkTheme) {
                    painterResource(id = R.drawable.ic_logo)
                } else {
                    painterResource(id = R.drawable.ic_logo_dark)
                },
                contentDescription = stringResource(id = R.string.peer),
                modifier = Modifier
                    .padding(vertical = 12.dp)
                    .height(56.dp)
            )
            FormHeader(
                title = stringResource(R.string.login_title).annotate(),
                description = stringResource(R.string.login_description),
                modifier = Modifier.padding(bottom = 28.dp)
            )
            LoginForm(
                email = email,
                password = password,
                isLoading = isLoading,
                error = error,
                rememberMe = rememberMe,
                onLogin = {
                    handleOnLogin(email.text.toString(), password.text.toString(), rememberMe.value)
                },
                onRegister = onRegister,
                onPasswordReset = {
                    handleOnPasswordReset(if (email.isValidEmail()) {
                        email.text.toString()
                    } else {
                        ""
                    })
                }
            )
        }
        Text(
            text = stringResource(R.string.privacy_text),
            style = MaterialTheme.typography.bodySmall.copy(
                textDecoration = TextDecoration.Underline,
            ),
            color = MaterialTheme.colorScheme.inverseSurface,
            modifier = Modifier
                .padding(
                    top = 36.dp,
                    bottom = 12.dp
                ).align(Alignment.CenterHorizontally)
                .clickable { handleOnPrivacy() }
        )
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun PreviewLoginPage() {
    PeerTheme {
        val isLoading = remember { mutableStateOf(false) }
        val error = remember { mutableStateOf(null) }
        LoginPage(
            login = null,
            isLoading = isLoading,
            error = error,
            onLogin = { _, _, _ -> },
            onPasswordReset = {},
            onPrivacy = {},
            onRegister = {}
        )
    }
}
