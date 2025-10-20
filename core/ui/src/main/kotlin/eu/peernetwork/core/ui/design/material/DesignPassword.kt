package eu.peernetwork.core.ui.design.material

import android.content.res.Configuration
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.KeyboardActionHandler
import androidx.compose.foundation.text.input.TextFieldDecorator
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.TextObfuscationMode
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.extension.passwordStrength
import eu.peernetwork.core.ui.theme.PeerAppGray85
import eu.peernetwork.core.ui.theme.PeerTheme

enum class DesignPasswordStrength(val value: Int) {
    BAD(0),
    WEAK(1),
    MEDIUM(2),
    GOOD(3),
    STRONG(4),
    EXCELLENT(5)
}

@Composable
fun DesignPassword(
    state: TextFieldState,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    showLabel: Boolean = false,
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
    label: @Composable (() -> Unit)? = null,
    indicator: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
) {
    DesignLabel(
        visible = showLabel,
        textStyle = textStyle,
        label = { label?.invoke() },
    ) {
        DesignSecureTextField(
            state = state,
            enabled = enabled,
            keyboardOptions = keyboardOptions,
            showLabel = true,
            durationMillis = durationMillis,
            delayMillis = delayMillis,
            easing = easing,
            label = { indicator?.invoke() },
            textObfuscationMode = textObfuscationMode,
            inputTransformation = inputTransformation,
            textStyle = textStyle,
            onKeyboardAction = onKeyboardAction,
            onTextLayout = onTextLayout,
            interactionSource = interactionSource,
            decorator = decorator,
            cursorBrush = cursorBrush,
            colors = colors,
            shape = shape,
            contentPadding = contentPadding,
            leading = leading,
            trailing = trailing,
            placeholder = placeholder,
            modifier = modifier,
        )
    }
}

@Composable
fun DesignPasswordIndicator(
    state: TextFieldState,
    modifier: Modifier = Modifier,
    space: Dp? = null,
    width: Dp? = null,
    shape: Shape = RoundedCornerShape(16.dp),
    colors: DesignIndicatorColors = DesignIndicatorColors.default,
    durationMillis: Int = 10,
    delayMillis: Int = 0,
    easing: Easing = FastOutSlowInEasing,
) {
    val density = LocalDensity.current
    val strength by remember(state) { derivedStateOf { state.passwordStrength() } }
    var parentHeight by remember { mutableStateOf(0.dp) }
    Row(
        modifier = modifier.onSizeChanged { size ->
            parentHeight = with(density) { size.height.toDp() }
        },
        horizontalArrangement = if (space == null) {
            Arrangement.SpaceBetween
        } else {
            Arrangement.Start
        }
    ) {
        repeat(DesignPasswordStrength.STRONG.value) { index ->
            key(index) {
                DesignIndicator(
                    modifier = if (width != null) {
                        Modifier.width(width)
                    } else {
                        Modifier.weight(1f)
                    }.height(parentHeight),
                    isActive = strength.value > index,
                    activeColor = if (strength.value > index) {
                        strength.getColor(colors)
                    } else  { colors.bad },
                    inActiveColor = colors.bad,
                    shape = shape,
                    durationMillis = durationMillis,
                    delayMillis = delayMillis,
                    easing = easing
                )
                if (index < DesignPasswordStrength.STRONG.value - 1) {
                    Spacer(modifier = if (space != null) {
                        Modifier.width(space)
                    } else {
                        Modifier.weight(1f)
                    })
                }
            }
        }
    }
}

private fun DesignPasswordStrength.getColor(colors: DesignIndicatorColors): Color {
    return when (this) {
        DesignPasswordStrength.BAD -> colors.bad
        DesignPasswordStrength.WEAK -> colors.weak
        DesignPasswordStrength.MEDIUM -> colors.medium
        DesignPasswordStrength.GOOD -> colors.good
        DesignPasswordStrength.STRONG -> colors.strong
        DesignPasswordStrength.EXCELLENT -> colors.excellent
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun PreviewDesignPassword() {
    PeerTheme {
        val state = TextFieldState("Password")
        Column {
            DesignPassword(
                state = state,
                modifier = Modifier,
                indicator = {
                    DesignPasswordIndicator(
                        state = state,
                        modifier = Modifier.padding(top = 16.dp)
                            .padding(horizontal = 16.dp)
                            .height(8.dp)
                    )
                }
            )
        }
    }
}
