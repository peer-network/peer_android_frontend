package eu.peernetwork.user.ui.v2.password.reset

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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
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
import eu.peernetwork.core.ui.theme.PeerAppGreen
import eu.peernetwork.core.ui.theme.PeerAppLightGreen
import eu.peernetwork.core.ui.theme.PeerAppRed
import eu.peernetwork.core.ui.theme.PeerAppYellow
import eu.peernetwork.user.ui.R
import eu.peernetwork.user.ui.compose.PasswordField

@Composable
fun ResetPage(
    enabled: Boolean,
    onFinish: () -> Unit
) {
    val password = remember { TextFieldState() }
    val confirmPassword = remember { TextFieldState() }
    val isValidated = remember { derivedStateOf {
        password.text.isNotEmpty() && password.text == confirmPassword.text
    } }
    val isPasswordVisible by remember { mutableStateOf(false) }
    val updatedOnFinish by rememberUpdatedState(onFinish)
    Column(
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
    ) {
        Text(
            text = stringResource(R.string.password_recovery_instruction),
            style = MaterialTheme.typography.displaySmall.copy(
                fontWeight = FontWeight.Normal
            ),
            color = MaterialTheme.colorScheme.onBackground
        )
        PasswordField(
            state = password,
            enabled = enabled,
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
        PasswordField(
            state = confirmPassword,
            hint = stringResource(id = R.string.confirm_password_label),
            enabled = enabled,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            modifier = Modifier.padding(top = 12.dp),
        )
        DesignButton(
            onClick = { updatedOnFinish() },
            enabled = isValidated.value,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
        ) { Text(stringResource(R.string.password_update_action)) }
    }
}
