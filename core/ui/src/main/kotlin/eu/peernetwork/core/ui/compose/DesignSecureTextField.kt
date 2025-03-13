package eu.peernetwork.core.ui.compose

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicSecureTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.KeyboardActionHandler
import androidx.compose.foundation.text.input.TextFieldDecorator
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.TextObfuscationMode
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.theme.PeerTheme

@Composable
fun DesignSecureTextField(
    state: TextFieldState,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    hasError: Boolean = false,
    durationMillis: Int = 10,
    delayMillis: Int = 0,
    easing: Easing = FastOutSlowInEasing,
    textObfuscationMode: TextObfuscationMode = TextObfuscationMode.Hidden,
    inputTransformation: InputTransformation? = null,
    textStyle: TextStyle = MaterialTheme.typography.bodyMedium,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    onKeyboardAction: KeyboardActionHandler? = null,
    onTextLayout: (Density.(getResult: () -> TextLayoutResult?) -> Unit)? = null,
    interactionSource: MutableInteractionSource? = null,
    decorator: TextFieldDecorator? = null,
    cursorBrush: Brush = SolidColor(MaterialTheme.colorScheme.primary),
    colors: TextFieldColors = DesignTextFieldColors.colors(),
    shape: Shape = RoundedCornerShape(16.dp),
    contentPadding: PaddingValues = PaddingValues(16.dp),
    leading: @Composable (() -> Unit)? = null,
    trailing: @Composable (() -> Unit)? = null,
    error: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
) {
    var isFocused by remember { mutableStateOf(false) }
    val backgroundColor by animateColorAsState(
        targetValue = if (isFocused) {
            colors.focusedContainerColor
        } else {
            colors.unfocusedContainerColor
        },
        animationSpec = tween(
            durationMillis = durationMillis,
            delayMillis = delayMillis,
            easing = easing
        )
    )
    val textColor by animateColorAsState(
        targetValue = if (isFocused) {
            colors.focusedTextColor
        } else {
            colors.unfocusedTextColor
        },
        animationSpec = tween(
            durationMillis = durationMillis,
            delayMillis = delayMillis,
            easing = easing
        )
    )
    val placeholderTextColor by animateColorAsState(
        targetValue = if (isFocused) {
            colors.focusedPlaceholderColor
        } else {
            colors.unfocusedPlaceholderColor
        },
        animationSpec = tween(
            durationMillis = durationMillis,
            delayMillis = delayMillis,
            easing = easing
        )
    )
    DesignLabel(
        label = error,
        visible = hasError,
        textStyle = MaterialTheme.typography.bodySmall.copy(
            color = colors.errorLabelColor
        ),
        modifier = modifier.onFocusChanged { isFocused = it.isFocused }
    ) {
        DesignCard(
            color = backgroundColor,
            shape = shape,
            contentPadding = contentPadding,
            leading = leading,
            trailing = trailing,
        ) {
            BasicSecureTextField(
                state = state,
                modifier = Modifier.fillMaxWidth(),
                enabled = enabled,
                inputTransformation = inputTransformation,
                textStyle = textStyle.copy(color = textColor),
                keyboardOptions = keyboardOptions,
                onKeyboardAction = onKeyboardAction,
                onTextLayout = onTextLayout,
                interactionSource = interactionSource,
                cursorBrush = cursorBrush,
                decorator = decorator,
                textObfuscationMode = textObfuscationMode
            )
            AnimatedVisibility(
                visible = state.text.isEmpty(),
                enter = fadeIn(),
                exit = fadeOut(),
            ) {
                placeholder?.run {
                    CompositionLocalProvider(
                        LocalTextStyle provides textStyle.copy(
                            color = placeholderTextColor
                        )
                    ) { this@run() }
                }
            }
        }
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun PreviewDesignSecureTextField() {
    PeerTheme {
        val text = remember { TextFieldState("Password") }
        val emptyPassword = remember { TextFieldState() }
        Column {
            DesignSecureTextField(
                state = emptyPassword,
                hasError = true,
                modifier = Modifier
                    .padding(bottom = 16.dp),
                error = {
                    Text(
                        text = "Opppps! Looks like error occurred!",
                        modifier = Modifier.padding(top = 8.dp, start = 16.dp)
                    )
                }
            ) { Text(text = "Placeholder") }
            DesignSecureTextField(state = text)
        }
    }
}
