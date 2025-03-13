package eu.peernetwork.user.ui.extension

import android.util.Patterns
import androidx.compose.foundation.text.input.TextFieldState

fun TextFieldState.isValidEmail(): Boolean {
    return Patterns.EMAIL_ADDRESS.matcher(text).matches()
}

fun TextFieldState.isValidInput(): Boolean {
    return text.trim().isNotEmpty()
}
