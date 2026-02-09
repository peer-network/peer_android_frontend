package eu.peernetwork.user.ui.extension

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import eu.peernetwork.feature.user.ui.R

fun TextFieldState.passwordRequirement(): List<Int> {
    val hasRequiredLength = text.length >= 8
    val hasUppercase = text.any { it.isUpperCase() }
    val hasLowercase = text.any { it.isLowerCase() }
    val hasDigit = text.any { it.isDigit() }
    return buildList {
        if (!hasRequiredLength) add(R.string.password_length_rule)
        if (!hasUppercase) add(R.string.password_uppercase_rule)
        if (!hasLowercase) add(R.string.password_lowercase_rule)
        if (!hasDigit) add(R.string.password_number_rule)
    }
}

@Composable
fun TextFieldState.policy(): Map<Int, String> {
    return passwordRequirement().associateWith { stringResource(it) }
}
