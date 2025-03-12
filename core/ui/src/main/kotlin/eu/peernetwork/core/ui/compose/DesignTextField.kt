package eu.peernetwork.core.ui.compose

import android.content.res.Configuration
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicSecureTextField
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.KeyboardActionHandler
import androidx.compose.foundation.text.input.OutputTransformation
import androidx.compose.foundation.text.input.TextFieldDecorator
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.TextObfuscationMode
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import eu.peernetwork.core.ui.theme.PeerTheme

@Composable
fun DesignTextField(
    state: TextFieldState,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    inputTransformation: InputTransformation? = null,
    textObfuscationMode: TextObfuscationMode? = null,
    textStyle: TextStyle = MaterialTheme.typography.bodyMedium,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    onKeyboardAction: KeyboardActionHandler? = null,
    lineLimits: TextFieldLineLimits = TextFieldLineLimits.SingleLine,
    onTextLayout: (Density.(getResult: () -> TextLayoutResult?) -> Unit)? = null,
    interactionSource: MutableInteractionSource? = null,
    decorator: TextFieldDecorator? = null,
    scrollState: ScrollState = rememberScrollState(),
    placeholder: @Composable (() -> Unit)? = null,
) {
    var isFocused by remember { mutableStateOf(false) }
    ConstraintLayout(modifier = modifier.clip(RoundedCornerShape(16.dp))
        .background(if (isFocused) {
            MaterialTheme.colorScheme.surfaceDim
        } else {
            MaterialTheme.colorScheme.surfaceBright
        }).padding(16.dp)) {
        val (textField, placeholderText) = createRefs()
        DesignTextEditor(
            state = state,
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(textField) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                }.onFocusChanged { isFocused = it.isFocused },
            enabled = enabled,
            readOnly = readOnly,
            inputTransformation = inputTransformation,
            textObfuscationMode = textObfuscationMode,
            textStyle = textStyle.copy(
                color = if (isFocused) {
                    MaterialTheme.colorScheme.surfaceTint
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                }
            ),
            keyboardOptions = keyboardOptions,
            onKeyboardAction = onKeyboardAction,
            lineLimits = lineLimits,
            onTextLayout = onTextLayout,
            interactionSource = interactionSource,
            cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
            decorator = decorator,
            scrollState = scrollState,
        )
        Box(
            modifier = Modifier.constrainAs(placeholderText) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    width = Dimension.matchParent
                }
        ) {
            CompositionLocalProvider(
                LocalTextStyle provides textStyle.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            ) {
                if (placeholder != null && state.text.isEmpty()) {
                    placeholder.invoke()
                }
            }
        }
    }
}

@Composable
private fun DesignTextEditor(
    state: TextFieldState,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textObfuscationMode: TextObfuscationMode? = null,
    inputTransformation: InputTransformation? = null,
    textStyle: TextStyle = MaterialTheme.typography.bodyMedium,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    onKeyboardAction: KeyboardActionHandler? = null,
    lineLimits: TextFieldLineLimits = TextFieldLineLimits.SingleLine,
    onTextLayout: (Density.(getResult: () -> TextLayoutResult?) -> Unit)? = null,
    interactionSource: MutableInteractionSource? = null,
    outputTransformation: OutputTransformation? = null,
    decorator: TextFieldDecorator? = null,
    cursorBrush: Brush,
    scrollState: ScrollState = rememberScrollState()
) {
    if (textObfuscationMode == null) {
        BasicTextField(
            state = state,
            modifier = modifier,
            enabled = enabled,
            readOnly = readOnly,
            inputTransformation = inputTransformation,
            textStyle = textStyle,
            keyboardOptions = keyboardOptions,
            onKeyboardAction = onKeyboardAction,
            lineLimits = lineLimits,
            onTextLayout = onTextLayout,
            interactionSource = interactionSource,
            cursorBrush = cursorBrush,
            outputTransformation = outputTransformation,
            decorator = decorator,
            scrollState = scrollState,
        )
    } else {
        BasicSecureTextField(
            state = state,
            modifier = modifier,
            enabled = enabled,
            inputTransformation = inputTransformation,
            textStyle = textStyle,
            keyboardOptions = keyboardOptions,
            onKeyboardAction = onKeyboardAction,
            onTextLayout = onTextLayout,
            interactionSource = interactionSource,
            cursorBrush = cursorBrush,
            decorator = decorator,
            textObfuscationMode = textObfuscationMode
        )
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun PreviewDesignTextEditor() {
    val text = remember { TextFieldState("Hello, world!") }
    PeerTheme {
        DesignTextField(
            state = text,
            textObfuscationMode = TextObfuscationMode.Hidden
        ) { Text("Hello, world!") }
    }
}
