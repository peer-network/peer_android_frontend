package eu.peernetwork.user.ui.v2.registration

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.design.luna.DesignButton
import eu.peernetwork.core.ui.design.luna.DesignTextField
import eu.peernetwork.core.ui.design.material.DesignIndicatorColors
import eu.peernetwork.core.ui.design.material.DesignPasswordIndicator
import eu.peernetwork.core.ui.extension.isValidEmail
import eu.peernetwork.core.ui.theme.PeerAppGreen
import eu.peernetwork.core.ui.theme.PeerAppLightGreen
import eu.peernetwork.core.ui.theme.PeerAppRed
import eu.peernetwork.core.ui.theme.PeerAppYellow
import eu.peernetwork.user.ui.R
import eu.peernetwork.user.ui.compose.LabelledCheckBox
import eu.peernetwork.user.ui.compose.PasswordField

@Composable
fun RegistrationForm(
    email: TextFieldState,
    username: TextFieldState,
    password: TextFieldState,
    policyAgreement: MutableState<Boolean>,
    licenceAgreement: MutableState<Boolean>,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onRegister: () -> Unit
) {
    val confirmPassword = remember { TextFieldState() }
    val isValidated = remember { derivedStateOf {
        email.isValidEmail()
                && username.text.isNotBlank()
                && policyAgreement.value && licenceAgreement.value
    } }
    Column(modifier = modifier) {
        DesignTextField(
            state = email,
            enabled = enabled,
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
        DesignTextField(
            state = username,
            enabled = enabled,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            hint = stringResource(id = R.string.username_label),
            modifier = Modifier.padding(top = 12.dp),
            leading = {
                Icon(
                    painter = painterResource(R.drawable.ic_email),
                    contentDescription = stringResource(id = R.string.username_label),
                    modifier = Modifier.padding(end = 8.dp)
                        .size(22.dp),
                    tint = LocalContentColor.current
                )
            }
        )
        PasswordField(
            state = password,
            enabled = enabled,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            modifier = Modifier.padding(top = 12.dp)
        )
        DesignPasswordIndicator(
            state = password,
            space = 8.dp,
            width = 24.dp,
            colors = DesignIndicatorColors(
                bad = MaterialTheme.colorScheme.outline,
                weak = PeerAppRed,
                medium = PeerAppYellow,
                good = PeerAppYellow,
                strong = PeerAppLightGreen,
                excellent = PeerAppGreen,
            ),
            modifier = Modifier
                .padding(top = 10.dp)
                .padding(horizontal = 18.dp)
                .height(2.dp)
        )
        PasswordField(
            state = confirmPassword,
            enabled = enabled,
            hint = stringResource(id = R.string.confirm_password_label),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            modifier = Modifier.padding(top = 12.dp)
        )
        LabelledCheckBox(
            state = policyAgreement,
            label = stringResource(R.string.privacy_agreement),
            modifier = Modifier.padding(top = 12.dp)
                .padding(horizontal = 18.dp)
        )
        LabelledCheckBox(
            state = licenceAgreement,
            label = stringResource(R.string.licence_agreement),
            modifier = Modifier.padding(top = 10.dp)
                .padding(horizontal = 18.dp)
        )
        DesignButton(
            onClick = onRegister,
            enabled = isValidated.value && enabled,
            modifier = Modifier.fillMaxWidth()
                .padding(top = 18.dp),
        ) { Text(stringResource(R.string.register_text)) }
    }
}
