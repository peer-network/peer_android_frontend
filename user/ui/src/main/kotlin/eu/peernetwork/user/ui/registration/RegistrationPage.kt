package eu.peernetwork.user.ui.registration

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.extension.annotate
import eu.peernetwork.user.ui.R
import eu.peernetwork.user.ui.form.FormHeader

private const val tag = "LOGIN"

data class RegistrationForm(
    val email: String,
    val username: String,
    val password: String,
)

@Composable
fun RegistrationPage(
    isLoading: State<Boolean>,
    error: State<String?>,
    onPrivacy: () -> Unit,
    onLicence: () -> Unit,
    onLogin: () -> Unit,
    onRegister: (RegistrationForm) -> Unit
) {
    val email by rememberSaveable(stateSaver = TextFieldState.Saver) { mutableStateOf(TextFieldState()) }
    val username by rememberSaveable(stateSaver = TextFieldState.Saver) { mutableStateOf(TextFieldState()) }
    val password by rememberSaveable(stateSaver = TextFieldState.Saver) { mutableStateOf(TextFieldState()) }
    val policyAgreement = remember { mutableStateOf(false) }
    val licenceAgreement = remember { mutableStateOf(false) }
    val annotatedString = buildAnnotatedString {
        append(stringResource(R.string.login_option))
        append(" ")
        pushStringAnnotation(tag = tag, annotation = tag)
        withStyle(
            style = SpanStyle(
                color = MaterialTheme.colorScheme.surfaceContainerHigh,
                textDecoration = TextDecoration.Underline,
                fontWeight = FontWeight.Medium
            )
        ) { append(stringResource(R.string.login_action)) }
        pop()
    }
    val handleOnPrivacy by rememberUpdatedState(onPrivacy)
    val handleOnLogin by rememberUpdatedState(onLogin)
    var layoutResult: TextLayoutResult? = null
    Column {
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 16.dp)
        ) {
            FormHeader(
                title = stringResource(R.string.register_text).annotate(),
                description = stringResource(R.string.register_slug),
                modifier = Modifier.padding(bottom = 32.dp)
            )
            RegistrationForm(
                email = email,
                username = username,
                password = password,
                onLicence = onLicence,
                onRegister = {
                    onRegister(
                        RegistrationForm(
                            email = email.text.toString(),
                            username = username.text.toString(),
                            password = password.text.toString()
                        )
                    )
                },
                onPrivacy = { handleOnPrivacy() },
                policyAgreement = policyAgreement,
                licenceAgreement = licenceAgreement,
                isLoading = isLoading,
                error = error
            )
            Text(
                text = annotatedString,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.scrim,
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .align(Alignment.CenterHorizontally)
                    .pointerInput(Unit) {
                    detectTapGestures { offset ->
                        layoutResult?.let { layout ->
                            val position = layout.getOffsetForPosition(offset)
                            annotatedString.getStringAnnotations(
                                tag = tag,
                                start = position,
                                end = position
                            ).firstOrNull()?.let { _ ->
                                handleOnLogin()
                            }
                        }
                    }
                },
                onTextLayout = { layoutResult = it }
            )
        }
    }
}
