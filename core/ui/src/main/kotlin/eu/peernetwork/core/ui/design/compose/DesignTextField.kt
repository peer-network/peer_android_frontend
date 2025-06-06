package eu.peernetwork.core.ui.design.compose

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.theme.PeerTheme

object DesignTextFieldColors {
    @Composable
    internal fun colors(): TextFieldColors {
        return TextFieldDefaults.colors(
            unfocusedContainerColor = MaterialTheme.colorScheme.tertiaryContainer,
            focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            focusedTextColor = MaterialTheme.colorScheme.onBackground,
            unfocusedTextColor = MaterialTheme.colorScheme.tertiary,
            focusedPlaceholderColor = MaterialTheme.colorScheme.surfaceDim,
            unfocusedPlaceholderColor = MaterialTheme.colorScheme.surfaceTint,
        )
    }
}

@Composable
fun DesignTextField(
    state: TextFieldState,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    hasError: Boolean = false,
    durationMillis: Int = 10,
    delayMillis: Int = 0,
    easing: Easing = FastOutSlowInEasing,
    textStyle: TextStyle = MaterialTheme.typography.bodyMedium,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    onKeyboardAction: KeyboardActions = KeyboardActions.Default,
    focusRequester: FocusRequester = FocusRequester(),
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
    interactionSource: MutableInteractionSource? = null,
    cursorBrush: Brush = SolidColor(MaterialTheme.colorScheme.primary),
    colors: TextFieldColors = DesignTextFieldColors.colors(),
    shape: Shape = RoundedCornerShape(16.dp),
    contentPadding: PaddingValues = PaddingValues(16.dp),
    singleLine: Boolean = false,
    maxLength: Int = Int.MAX_VALUE,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    minLines: Int = 1,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    onTextLayout: (TextLayoutResult) -> Unit = {},
    decorationBox: @Composable (innerTextField: @Composable () -> Unit) -> Unit =
        @Composable { innerTextField -> innerTextField() },
    leading: @Composable (() -> Unit)? = null,
    trailing: @Composable (() -> Unit)? = null,
    error: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
) {
    var isFocused by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()
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
    val updatePlaceholder by rememberUpdatedState(placeholder)
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
            horizontalArrangement = horizontalArrangement,
            verticalAlignment = verticalAlignment
        ) {
            BasicTextField(
                value = state.text.toString(),
                onValueChange = { input ->
                    if (maxLength == Int.MAX_VALUE || input.length <= maxLength) {
                        state.edit { replace(0, length, input) }
                    }
                },
                modifier = Modifier.fillMaxWidth()
                    .horizontalScroll(scrollState)
                    .focusRequester(focusRequester),
                enabled = enabled,
                readOnly = readOnly,
                textStyle = textStyle.copy(color = textColor),
                keyboardActions = onKeyboardAction,
                keyboardOptions = keyboardOptions,
                interactionSource = interactionSource,
                cursorBrush = cursorBrush,
                maxLines = maxLines,
                minLines = minLines,
                visualTransformation = visualTransformation,
                onTextLayout = onTextLayout,
                decorationBox = decorationBox
            )
            AnimatedVisibility(
                visible = state.text.isEmpty(),
                enter = fadeIn(),
                exit = fadeOut(),
            ) {
                updatePlaceholder?.run {
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
fun PreviewDesignTextEditor() {
    val text = remember { TextFieldState("Hello, world!") }
    val emptyText = remember { TextFieldState() }
    PeerTheme {
        Column {
            DesignTextField(
                state = emptyText,
                modifier = Modifier.padding(bottom = 16.dp),
                hasError = true,
                error = {
                    Text(
                        text = "Opppps! Looks like error occured!",
                        modifier = Modifier.padding(top = 8.dp, start = 16.dp)
                    )
                }
            ) { Text(text = "Placeholder") }
            DesignTextField(
                state = text,
            ) { Text("Hello, world!") }
        }
    }
}
