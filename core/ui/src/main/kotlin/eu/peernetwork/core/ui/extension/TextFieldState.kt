package eu.peernetwork.core.ui.extension

import android.util.Patterns
import androidx.compose.foundation.text.input.TextFieldState
import eu.peernetwork.core.ui.design.material.DesignPasswordStrength

val TextFieldState.value: String get() = text.toString()

fun TextFieldState.isValidEmail(): Boolean {
    return Patterns.EMAIL_ADDRESS.matcher(text).matches()
}

fun TextFieldState.isValidInput(): Boolean {
    return text.trim().isNotEmpty()
}

fun TextFieldState.passwordStrength(): DesignPasswordStrength {
    val hasRequiredLength = text.length >= 8
    val hasUppercase = text.any { it.isUpperCase() }
    val hasLowercase = text.any { it.isLowerCase() }
    val hasDigit = text.any { it.isDigit() }
    val hasSpecialCharacter = text.any { !it.isLetterOrDigit() }
    var score = hasRequiredLength.toInt() +
            hasUppercase.toInt() +
            hasLowercase.toInt() +
            hasDigit.toInt()
    if (score == DesignPasswordStrength.STRONG.value) {
        score += (hasSpecialCharacter || text.length >= 12).toInt()
    }
    return when (score) {
        DesignPasswordStrength.BAD.value -> DesignPasswordStrength.BAD
        DesignPasswordStrength.WEAK.value -> DesignPasswordStrength.WEAK
        DesignPasswordStrength.MEDIUM.value -> DesignPasswordStrength.MEDIUM
        DesignPasswordStrength.GOOD.value -> DesignPasswordStrength.GOOD
        DesignPasswordStrength.STRONG.value -> DesignPasswordStrength.STRONG
        DesignPasswordStrength.EXCELLENT.value -> DesignPasswordStrength.EXCELLENT
        else -> DesignPasswordStrength.WEAK
    }
}
