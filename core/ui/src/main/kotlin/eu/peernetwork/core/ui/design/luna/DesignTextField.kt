package eu.peernetwork.core.ui.design.luna

import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.R
import eu.peernetwork.core.ui.theme.DesignTheme

@Composable
fun DesignTextField(
    modifier: Modifier = Modifier,
    state: TextFieldState = remember { TextFieldState() },
    enabled: Boolean = true,
    readOnly: Boolean = false,
    hint: String? = null,
    color: Color = MaterialTheme.colorScheme.surfaceContainerLow,
    unFocusedColor: Color = MaterialTheme.colorScheme.surfaceContainerLowest,
    hintColor: Color = MaterialTheme.colorScheme.scrim,
    unFocusedHintColor: Color = MaterialTheme.colorScheme.outlineVariant,
    contentColor: Color = MaterialTheme.colorScheme.onBackground,
    unFocusedContentColor: Color = MaterialTheme.colorScheme.outline,
    shape: Shape = CircleShape,
    contentPadding: PaddingValues = PaddingValues(
        horizontal = 16.dp,
        vertical = 14.dp
    ),
    textStyle: TextStyle = MaterialTheme.typography.bodyMedium,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    onKeyboardAction: KeyboardActions = KeyboardActions.Default,
    minLines: Int = 1,
    maxLength: Int = Int.MAX_VALUE,
    maxLines: Int = Int.MAX_VALUE,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    onTextLayout: (TextLayoutResult) -> Unit = {},
    interactionSource: MutableInteractionSource? = null,
    cursorBrush: Brush = SolidColor(MaterialTheme.colorScheme.primary),
    durationMillis: Int = 10,
    delayMillis: Int = 0,
    easing: Easing = FastOutSlowInEasing,
    leading: @Composable RowScope.() -> Unit = {},
    trailing: @Composable RowScope.() -> Unit = {},
) {
    var isFocused by remember { mutableStateOf(false) }
    val updatedLeading by rememberUpdatedState(leading)
    val updatedTrailing by rememberUpdatedState(trailing)
    val backgroundColor by animateColorAsState(
        targetValue = if (isFocused && enabled) {
            color
        } else {
            unFocusedColor
        },
        animationSpec = tween(
            durationMillis = durationMillis,
            delayMillis = delayMillis,
            easing = easing
        )
    )
    val contentColor by animateColorAsState(
        targetValue = if (isFocused && enabled) {
            contentColor
        } else {
            unFocusedContentColor
        },
        animationSpec = tween(
            durationMillis = durationMillis,
            delayMillis = delayMillis,
            easing = easing
        )
    )
    val hintContentColor by animateColorAsState(
        targetValue = if (isFocused) {
            hintColor
        } else {
            unFocusedHintColor
        },
        animationSpec = tween(
            durationMillis = durationMillis,
            delayMillis = delayMillis,
            easing = easing
        )
    )
    Box(
        modifier = modifier.clip(shape)
            .background(backgroundColor)
            .border(
                width = 1.dp,
                color = backgroundColor,
                shape = shape
            ).padding(contentPadding)
            .onFocusChanged { isFocused = it.isFocused }
    ) {
        CompositionLocalProvider(
            LocalContentColor provides if (readOnly) {
                hintContentColor
            } else {
                contentColor
            },
            LocalTextStyle provides textStyle.copy(
                color = if (readOnly) {
                    hintContentColor
                } else {
                    contentColor
                }
            ),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                updatedLeading()
                Box(modifier = Modifier.weight(1f)) {
                    BasicTextField(
                        value = state.text.toString(),
                        onValueChange = { input ->
                            if (maxLength == Int.MAX_VALUE || input.length <= maxLength) {
                                state.edit { replace(0, length, input) }
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = enabled,
                        readOnly = readOnly,
                        textStyle = LocalTextStyle.current,
                        keyboardActions = onKeyboardAction,
                        keyboardOptions = keyboardOptions,
                        interactionSource = interactionSource,
                        cursorBrush = cursorBrush,
                        maxLines = maxLines,
                        minLines = minLines,
                        onTextLayout = onTextLayout,
                        visualTransformation = visualTransformation
                    )
                    Crossfade(state.text.isEmpty()) { targetState ->
                        if (targetState) {
                            Text(
                                text = hint ?: "",
                                style = LocalTextStyle.current,
                                color = hintContentColor
                            )
                        }
                    }
                }
                updatedTrailing()
            }
        }
    }
}

@Preview
@Composable
fun DarkDesignTextFieldPreview() {
    DesignTheme(isDarkMode = true) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            DesignTextField(
                state = remember { TextFieldState("Hello, World!") },
                modifier = Modifier.fillMaxWidth(),
                leading = {
                    Icon(
                        painter = painterResource(R.drawable.ic_trend_outline),
                        contentDescription = null,
                        modifier = Modifier.padding(end = 8.dp)
                            .size(22.dp),
                        tint = LocalContentColor.current
                    )
                }
            )
            DesignTextField(
                state = remember { TextFieldState("Hello, world!") },
                modifier = Modifier.fillMaxWidth(),
            )
            DesignTextField(
                state = remember { TextFieldState("Read-only") },
                modifier = Modifier.fillMaxWidth(),
                enabled = false,
                readOnly = true
            )
            DesignTextField(
                state = remember { TextFieldState() },
                hint = "Hint text",
                modifier = Modifier.fillMaxWidth(),
                enabled = true,
            )
        }
    }
}

@Preview
@Composable
fun LightDesignTextFieldPreview() {
    DesignTheme(isDarkMode = false) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            DesignTextField(
                state = remember { TextFieldState("Hello, World!") },
                modifier = Modifier.fillMaxWidth(),
            )
            DesignTextField(
                state = remember { TextFieldState("Hello, world!") },
                modifier = Modifier.fillMaxWidth(),
            )
            DesignTextField(
                state = remember { TextFieldState("Read-only") },
                modifier = Modifier.fillMaxWidth(),
                enabled = false,
            )
            DesignTextField(
                state = remember { TextFieldState() },
                hint = "Hint text",
                modifier = Modifier.fillMaxWidth(),
                enabled = true,
            )
        }
    }
}
