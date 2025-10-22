package eu.peernetwork.user.ui.v2.registration

import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.design.luna.DesignButton
import eu.peernetwork.core.ui.design.luna.DesignTextField
import eu.peernetwork.core.ui.design.material.DesignIndicatorColors
import eu.peernetwork.core.ui.design.material.DesignPasswordIndicator
import eu.peernetwork.core.ui.design.material.DesignPasswordStrength
import eu.peernetwork.core.ui.extension.isValidEmail
import eu.peernetwork.core.ui.extension.passwordStrength
import eu.peernetwork.core.ui.theme.PeerAppGreen
import eu.peernetwork.core.ui.theme.PeerAppLightGreen
import eu.peernetwork.core.ui.theme.PeerAppRed
import eu.peernetwork.core.ui.theme.PeerAppYellow
import eu.peernetwork.user.ui.R
import eu.peernetwork.user.ui.compose.ErrorLabel
import eu.peernetwork.user.ui.compose.LabelledCheckBox
import eu.peernetwork.user.ui.compose.PasswordField

private const val tag = "LINK"

@Composable
fun RegistrationForm(
    email: TextFieldState,
    username: TextFieldState,
    password: TextFieldState,
    policyAgreement: MutableState<Boolean>,
    licenceAgreement: MutableState<Boolean>,
    isLoading: State<Boolean>,
    error: State<String?>,
    modifier: Modifier = Modifier,
    onPrivacy: () -> Unit,
    onLicence: () -> Unit,
    onRegister: () -> Unit
) {
    val confirmPassword = remember { TextFieldState() }
    val isValidated = remember { derivedStateOf {
        email.isValidEmail()
                && username.text.isNotBlank()
                && policyAgreement.value && licenceAgreement.value
                && password.text == confirmPassword.text
                && password.passwordStrength().value >= DesignPasswordStrength.STRONG.value
    } }
    val privacy = buildAnnotatedString {
        append(stringResource(R.string.agreement))
        append(" ")
        pushStringAnnotation(tag = tag, annotation = tag)
        withStyle(
            style = SpanStyle(
                color = MaterialTheme.colorScheme.outline
            )
        ) { append(stringResource(R.string.privacy_agreement)) }
        pop()
    }
    val licence = buildAnnotatedString {
        append(stringResource(R.string.agreement))
        append(" ")
        pushStringAnnotation(tag = tag, annotation = tag)
        withStyle(
            style = SpanStyle(
                color = MaterialTheme.colorScheme.outline
            )
        ) { append(stringResource(R.string.licence_agreement)) }
        pop()
    }
    val layoutResult = remember { mutableStateOf<TextLayoutResult?>(null) }
    val handleOnPrivacy by rememberUpdatedState(onPrivacy)
    val handleOnLicence by rememberUpdatedState(onLicence)
    val passwordValidationError = stringResource(R.string.password_mismatch)
    val validationError = remember { derivedStateOf {
        if (password.text != confirmPassword.text) {
            passwordValidationError
        } else {
            null
        }
    } }
    val errorState = remember(error.value) { mutableStateOf(error.value) }
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
        DesignTextField(
            state = username,
            enabled = !isLoading.value,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            hint = stringResource(id = R.string.username_label),
            modifier = Modifier.padding(top = 12.dp),
            leading = {
                Icon(
                    painter = painterResource(R.drawable.ic_user),
                    contentDescription = stringResource(id = R.string.username_label),
                    modifier = Modifier.padding(end = 8.dp)
                        .size(22.dp),
                    tint = LocalContentColor.current
                )
            }
        )
        PasswordField(
            state = password,
            enabled = !isLoading.value,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            modifier = Modifier.padding(top = 12.dp)
        )
        DesignPasswordIndicator(
            state = password,
            space = 4.dp,
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
            enabled = !isLoading.value,
            hint = stringResource(id = R.string.confirm_password_label),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            modifier = Modifier.padding(top = 12.dp)
        )
        LabelledCheckBox(
            state = policyAgreement,
            label = privacy,
            modifier = Modifier.padding(top = 12.dp)
                .padding(horizontal = 18.dp)
                .pointerInput(Unit) {
                    detectTapGestures { offset ->
                        layoutResult.value?.let { layout ->
                            val position = layout.getOffsetForPosition(offset)
                            licence.getStringAnnotations(
                                tag = tag,
                                start = position,
                                end = position
                            ).firstOrNull()?.let { _ ->
                                handleOnPrivacy()
                            }
                        }
                    }
                },
        )
        LabelledCheckBox(
            state = licenceAgreement,
            label = licence,
            textLayoutResult = layoutResult,
            modifier = Modifier.padding(top = 10.dp, bottom = 8.dp)
                .padding(horizontal = 18.dp)
                .pointerInput(Unit) {
                    detectTapGestures { offset ->
                        layoutResult.value?.let { layout ->
                            val position = layout.getOffsetForPosition(offset)
                            licence.getStringAnnotations(
                                tag = tag,
                                start = position,
                                end = position
                            ).firstOrNull()?.let { _ ->
                                handleOnLicence()
                            }
                        }
                    }
                },
        )
        ErrorLabel(
            error = errorState,
            modifier = Modifier.padding(horizontal = 18.dp)
        )
        DesignButton(
            onClick = onRegister,
            isLoading = isLoading.value,
            enabled = isValidated.value && !isLoading.value,
            modifier = Modifier.fillMaxWidth()
                .padding(top = 10.dp),
        ) {
            Text(stringResource(R.string.register_text))
            LaunchedEffect(validationError.value) {
                if (validationError.value != null) {
                    errorState.value = validationError.value
                } else {
                    errorState.value = error.value
                }
            }
        }
    }
}
