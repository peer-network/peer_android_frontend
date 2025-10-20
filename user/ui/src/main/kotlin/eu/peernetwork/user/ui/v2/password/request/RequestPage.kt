package eu.peernetwork.user.ui.v2.password.request

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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.design.luna.DesignButton
import eu.peernetwork.core.ui.design.luna.DesignTextField
import eu.peernetwork.core.ui.extension.annotate
import eu.peernetwork.core.ui.extension.isValidEmail
import eu.peernetwork.user.ui.R
import eu.peernetwork.user.ui.compose.FormHeader

@Composable
fun RequestPage(
    email: String,
    onReset: (String) -> Unit
) {
    val email = remember { TextFieldState(email) }
    val isValidated = remember { derivedStateOf { email.isValidEmail() } }
    val updatedOnReset by rememberUpdatedState(onReset)
    Column(
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
    ) {
        FormHeader(
            title = stringResource(R.string.forgot_password_text).annotate(
                text = stringResource(R.string.peer).lowercase(),
                style = SpanStyle(
                    fontStyle = FontStyle.Italic,
                    fontWeight = FontWeight.Bold
                )
            ),
            description = stringResource(R.string.forgot_instruction),
            modifier = Modifier.padding(bottom = 32.dp)
        )
        DesignTextField(
            state = email,
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
            }
        )
        DesignButton(
            onClick = { updatedOnReset(email.text.toString()) },
            enabled = isValidated.value,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
        ) { Text(stringResource(R.string.send_text)) }
    }
}
