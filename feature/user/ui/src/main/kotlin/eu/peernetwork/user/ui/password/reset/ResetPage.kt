package eu.peernetwork.user.ui.password.reset

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.design.luna.DesignButton
import eu.peernetwork.core.ui.design.material.DesignIndicatorColors
import eu.peernetwork.core.ui.design.material.DesignPasswordIndicator
import eu.peernetwork.core.ui.design.material.DesignPasswordStrength
import eu.peernetwork.core.ui.extension.passwordStrength
import eu.peernetwork.core.ui.theme.PeerAppGreen
import eu.peernetwork.core.ui.theme.PeerAppLightGreen
import eu.peernetwork.core.ui.theme.PeerAppRed
import eu.peernetwork.core.ui.theme.PeerAppYellow
import eu.peernetwork.feature.user.ui.R
import eu.peernetwork.user.ui.form.FormError
import eu.peernetwork.user.ui.form.FormPassword
import eu.peernetwork.user.ui.extension.passwordRequirement
import eu.peernetwork.user.ui.extension.policy

@Composable
fun ResetPage(
    isLoading: State<Boolean>,
    error: State<String?>,
    onFinish: (String) -> Unit
) {
    val password = remember { TextFieldState() }
    val confirmPassword = remember { TextFieldState() }
    val isValidated = remember { derivedStateOf {
        password.text == confirmPassword.text &&
                password.passwordStrength().value >= DesignPasswordStrength.STRONG.value
    } }
    val mismatchError = stringResource(R.string.password_mismatch)
    val derivedError = remember { derivedStateOf {
        when {
            confirmPassword.text.isNotEmpty() && password.text != confirmPassword.text ->
                mismatchError
            error.value != null -> error.value
            else -> null
        }
    } }
    val updatedOnFinish by rememberUpdatedState(onFinish)
    val policy = password.policy()
    val separator = stringResource(R.string.password_separator)
    val minimumLabel = stringResource(R.string.password_rule_label)
    val passwordValidationError = remember { derivedStateOf {
        val requirements = password.passwordRequirement()
        if (requirements.isEmpty() || password.text.isEmpty()) {
            null
        } else {
            "$minimumLabel " + requirements
                .joinToString(separator) { policy[it].toString() }
        }
    } }
    Column(
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = stringResource(R.string.password_recovery_instruction),
            style = MaterialTheme.typography.displaySmall.copy(
                fontWeight = FontWeight.Normal
            ),
            color = MaterialTheme.colorScheme.onBackground
        )
        FormPassword(
            state = password,
            enabled = !isLoading.value,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.padding(top = 16.dp)
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
        FormError(
            error = passwordValidationError,
            modifier = Modifier.padding(horizontal = 18.dp)
                .padding(top = 8.dp)
        )
        FormPassword(
            state = confirmPassword,
            hint = stringResource(id = R.string.confirm_password_label),
            enabled = !isLoading.value,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            modifier = Modifier.padding(top = 12.dp, bottom = 8.dp),
        )
        FormError(
            error = derivedError,
            modifier = Modifier.padding(horizontal = 18.dp)
        )
        DesignButton(
            onClick = { updatedOnFinish(password.text.toString()) },
            enabled = isValidated.value && !isLoading.value,
            isLoading = isLoading.value,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
        ) { Text(stringResource(R.string.password_update_action)) }
    }
}
