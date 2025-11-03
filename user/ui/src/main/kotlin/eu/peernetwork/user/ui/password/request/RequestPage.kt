package eu.peernetwork.user.ui.password.request

import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.design.luna.DesignButton
import eu.peernetwork.core.ui.design.luna.DesignTextField
import eu.peernetwork.core.ui.extension.isValidEmail
import eu.peernetwork.user.ui.R
import eu.peernetwork.user.ui.form.ErrorLabel
import eu.peernetwork.user.ui.form.FormHeader

private const val tag = "REQUEST_PASSWORD_TAG"

@Composable
fun RequestPage(
    email: String,
    isLoading: State<Boolean>,
    error: State<String?>,
    onVerify: () -> Unit,
    onReset: (String) -> Unit
) {
    val email by rememberSaveable(stateSaver = TextFieldState.Saver) {
        mutableStateOf(TextFieldState(email))
    }
    val isValidated = remember { derivedStateOf { email.isValidEmail() } }
    val updatedOnReset by rememberUpdatedState(onReset)
    val updatedOnVerify by rememberUpdatedState(onVerify)
    var layoutResult: TextLayoutResult? = null
    Column(
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
    ) {
        val description = buildAnnotatedString {
            append(stringResource(R.string.forgot_instruction))
        }
        FormHeader(
            title = { Text(text = stringResource(R.string.forgot_password_text)) },
            modifier = Modifier
                .padding(bottom = 32.dp),
        ) {
            Text(
                text = description,
                modifier = Modifier.pointerInput(Unit) {
                    detectTapGestures { offset ->
                        layoutResult?.let { layout ->
                            val position = layout.getOffsetForPosition(offset)
                            description.getStringAnnotations(
                                tag = tag,
                                start = position,
                                end = position
                            ).firstOrNull()?.let { _ ->
                                updatedOnVerify()
                            }
                        }
                    }
                },
                onTextLayout = { layoutResult = it }
            )
        }
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
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .size(22.dp),
                    tint = LocalContentColor.current
                )
            },
            modifier = Modifier.padding(bottom = 6.dp)
        )
        ErrorLabel(
            error = error,
            modifier = Modifier.padding(horizontal = 18.dp)
        )
        DesignButton(
            onClick = { updatedOnReset(email.text.toString()) },
            enabled = !isLoading.value && isValidated.value,
            isLoading = isLoading.value,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 6.dp),
        ) { Text(stringResource(R.string.send_text)) }
    }
}
