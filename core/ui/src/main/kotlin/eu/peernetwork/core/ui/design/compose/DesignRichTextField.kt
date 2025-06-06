package eu.peernetwork.core.ui.design.compose

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.theme.PeerTheme

@Composable
fun DesignRichTextField(
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
    mentionColor: Color = MaterialTheme.colorScheme.primary,
    hashtagColor: Color = MaterialTheme.colorScheme.primary,
    linkColor: Color = MaterialTheme.colorScheme.primary,
    onTextLayout: (TextLayoutResult) -> Unit = {},
    leading: @Composable (() -> Unit)? = null,
    trailing: @Composable (() -> Unit)? = null,
    error: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null
) {
    var isFocused by remember { mutableStateOf(false) }
    val pattern = remember { Regex("""(@\w+)|(#\w+)|(https?://[^\s]+)|(ftp://[^\s]+)""", RegexOption.IGNORE_CASE) }
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
    val defaultTextColor by animateColorAsState(
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
    val annotatedString by derivedStateOf {
        buildAnnotatedString {
            val matches = pattern.findAll(state.text)
            var lastIndex = 0

            matches.forEach { match ->
                append(state.text.substring(lastIndex, match.range.first))
                withStyle(
                    SpanStyle(
                        color = when {
                            match.value.startsWith("@", true) -> mentionColor
                            match.value.startsWith("#", true) -> hashtagColor
                            match.value.startsWith("http", true) -> linkColor
                            match.value.startsWith("ftp", true) -> linkColor
                            else -> defaultTextColor
                        }
                    )
                ) {
                    append(match.value)
                }
                lastIndex = match.range.last + 1
            }
            if (lastIndex < state.text.length) {
                append(state.text.substring(lastIndex))
            }
        }
    }
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
            Box {
                BasicTextField(
                    value = state.text.toString(),
                    onValueChange = { input ->
                        if (maxLength == Int.MAX_VALUE || input.length <= maxLength) {
                            state.edit { replace(0, length, input) }
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                        .focusRequester(focusRequester),
                    enabled = enabled,
                    readOnly = readOnly,
                    textStyle = textStyle.copy(color = defaultTextColor),
                    keyboardActions = onKeyboardAction,
                    keyboardOptions = keyboardOptions,
                    interactionSource = interactionSource,
                    cursorBrush = cursorBrush,
                    maxLines = maxLines,
                    minLines = minLines,
                    onTextLayout = onTextLayout,
                    visualTransformation = VisualTransformation {
                        TransformedText(annotatedString, OffsetMapping.Identity)
                    }
                )
            }
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

@Preview(showBackground = true)
@Composable
fun DesignAnnotatedTextPreview() {
    PeerTheme {
        Column(modifier = Modifier.padding(16.dp)) {
            val state = remember { TextFieldState("Hello @you, check #Jetpack at https://developer.android.com") }

            DesignRichTextField(
                state = state,
                mentionColor = Color.Red,
                hashtagColor = Color.Yellow,
                linkColor = Color.Green
            )
        }
    }
}
