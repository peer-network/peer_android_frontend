package eu.peernetwork.user.ui.compose.password

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.TextObfuscationMode
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.design.luna.DesignSecuredTextField
import eu.peernetwork.user.ui.R

@Composable
fun PasswordField(
    state: TextFieldState,
    modifier: Modifier = Modifier,
    hint: String = stringResource(id = R.string.password_label),
    enabled: Boolean = true,
    keyboardOptions: KeyboardOptions = KeyboardOptions(
        keyboardType = KeyboardType.Password,
        imeAction = ImeAction.Done
    ),
) {
    val obfuscationMode = remember { mutableStateOf(TextObfuscationMode.Hidden) }
    DesignSecuredTextField(
        state = state,
        enabled = enabled,
        hint = hint,
        keyboardOptions = keyboardOptions,
        textObfuscationMode = obfuscationMode.value,
        modifier = modifier,
        leading = {
            Icon(
                painter = painterResource(R.drawable.ic_lock),
                contentDescription = null,
                modifier = Modifier.padding(end = 8.dp)
                    .size(22.dp),
                tint = LocalContentColor.current
            )
        },
        trailing = {
            Crossfade(obfuscationMode.value) { target ->
                Icon(
                    painter = painterResource(if (target == TextObfuscationMode.Hidden) {
                        R.drawable.ic_eye_opened
                    } else {
                        R.drawable.ic_eye_closed
                    }),
                    contentDescription = hint,
                    modifier = Modifier.padding(start = 8.dp)
                        .size(22.dp)
                        .clip(CircleShape)
                        .clickable {
                            obfuscationMode.value = if (obfuscationMode.value == TextObfuscationMode.Hidden) {
                                TextObfuscationMode.Visible
                            } else {
                                TextObfuscationMode.Hidden
                            }
                        },
                    tint = MaterialTheme.colorScheme.scrim
                )
            }
        }
    )
}
