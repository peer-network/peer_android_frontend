package eu.peernetwork.core.ui.design.luna

import android.content.res.Configuration
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.R
import eu.peernetwork.core.ui.theme.DesignTheme

@Composable
fun DesignText(
    text: AnnotatedString,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    fontSize: TextUnit = TextUnit.Unspecified,
    fontStyle: FontStyle? = null,
    fontWeight: FontWeight? = null,
    fontFamily: FontFamily? = null,
    letterSpacing: TextUnit = TextUnit.Unspecified,
    textDecoration: TextDecoration? = null,
    textAlign: TextAlign? = null,
    lineHeight: TextUnit = TextUnit.Unspecified,
    overflow: TextOverflow = TextOverflow.Clip,
    softWrap: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1,
    expandEllipsis: String = stringResource(R.string.show_more),
    collapseEllipsis: String = stringResource(R.string.show_less),
    inlineContent: Map<String, InlineTextContent> = mapOf(),
    onExpand: (Boolean) -> Unit = {},
    onTap: (Offset) -> Unit = {},
    onTextLayout: (TextLayoutResult) -> Unit = {},
    style: TextStyle = LocalTextStyle.current
) {
    val handleOnExpand by rememberUpdatedState(onExpand)
    val handleOnTap by rememberUpdatedState(onTap)
    val handleOnTextLayout by rememberUpdatedState(onTextLayout)
    var layoutResult by remember { mutableStateOf<TextLayoutResult?>(null) }
    var adjustedText by remember { mutableStateOf(text) }
    var hasOverflow by remember { mutableStateOf(false) }
    var expanded by rememberSaveable { mutableStateOf(false) }
    Text(
        text = adjustedText,
        modifier = modifier
            .pointerInput(Unit) {
                detectTapGestures { offset ->
                    layoutResult?.let { layout ->
                        val position = layout.getOffsetForPosition(offset)
                        val annotations = adjustedText.getStringAnnotations(
                            tag = expandEllipsis,
                            start = position,
                            end = position
                        )
                        if (annotations.isNotEmpty()) {
                            expanded = !expanded
                        } else {
                            handleOnTap(offset)
                        }
                    } ?: run { handleOnTap(offset) }
                }
            },
        color = color,
        fontSize = fontSize,
        fontStyle = fontStyle,
        fontWeight = fontWeight,
        fontFamily = fontFamily,
        letterSpacing = letterSpacing,
        textDecoration = textDecoration,
        textAlign = textAlign,
        lineHeight = lineHeight,
        overflow = overflow,
        softWrap = softWrap,
        maxLines = if (expanded) Int.MAX_VALUE else maxLines,
        minLines = minLines,
        inlineContent = inlineContent,
        onTextLayout = { result ->
            layoutResult = result
            if (result.hasVisualOverflow && !hasOverflow) {
                hasOverflow = true
            }
            handleOnTextLayout(result)
        },
        style = style
    )
    LaunchedEffect(expanded) {
        if (expanded) {
            adjustedText = buildAnnotatedString {
                append(text)
                pushStringAnnotation(tag = expandEllipsis, annotation = expandEllipsis)
                withStyle(style = SpanStyle(fontWeight = FontWeight.SemiBold)) {
                    append(collapseEllipsis)
                }
                pop()
            }
        } else if (hasOverflow) {
            hasOverflow = false
            layoutResult?.let { result ->
                 try {
                    result.getLineEnd(maxLines - 1, visibleEnd = true)
                } catch (_: Throwable) {
                   null
                }
            }?.let { result ->
                val cutoffIndex = (result - expandEllipsis.length).coerceAtLeast(0)
                adjustedText = buildAnnotatedString {
                    append(text.subSequence(0, cutoffIndex))
                    pushStringAnnotation(tag = expandEllipsis, annotation = expandEllipsis)
                    withStyle(style = SpanStyle(fontWeight = FontWeight.SemiBold)) {
                        append(expandEllipsis)
                    }
                    pop()
                }
            }
        }
    }
    LaunchedEffect(hasOverflow, text, expandEllipsis) {
        if (hasOverflow) {
            layoutResult?.let { result ->
                try {
                    result.getLineEnd(maxLines - 1, visibleEnd = true)
                } catch (_: Throwable) {
                    null
                }
            }?.let { result ->
                val cutoffIndex = (result - expandEllipsis.length).coerceAtLeast(0)
                adjustedText = buildAnnotatedString {
                    append(text.subSequence(0, cutoffIndex))
                    pushStringAnnotation(tag = expandEllipsis, annotation = expandEllipsis)
                    withStyle(style = SpanStyle(fontWeight = FontWeight.SemiBold)) {
                        append(expandEllipsis)
                    }
                    pop()
                }
            }
        }
        handleOnExpand(expanded)
    }
}

@Composable
fun DesignText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    fontSize: TextUnit = TextUnit.Unspecified,
    fontStyle: FontStyle? = null,
    fontWeight: FontWeight? = null,
    fontFamily: FontFamily? = null,
    letterSpacing: TextUnit = TextUnit.Unspecified,
    textDecoration: TextDecoration? = null,
    textAlign: TextAlign? = null,
    lineHeight: TextUnit = TextUnit.Unspecified,
    overflow: TextOverflow = TextOverflow.Clip,
    softWrap: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1,
    inlineContent: Map<String, InlineTextContent> = mapOf(),
    onTextLayout: (TextLayoutResult) -> Unit = {},
    style: TextStyle = LocalTextStyle.current
) {
    DesignText(
        text = buildAnnotatedString { append(text) },
        modifier = modifier,
        color = color,
        fontSize = fontSize,
        fontStyle = fontStyle,
        fontWeight = fontWeight,
        fontFamily = fontFamily,
        letterSpacing = letterSpacing,
        textDecoration = textDecoration,
        textAlign = textAlign,
        lineHeight = lineHeight,
        overflow = overflow,
        softWrap = softWrap,
        maxLines = maxLines,
        minLines = minLines,
        inlineContent = inlineContent,
        onTextLayout = onTextLayout,
        style = style
    )
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun DesignTextPreview() {
    DesignTheme {
        Column {
            DesignText(
                text = "Hello, world",
                maxLines = 3,
                modifier = Modifier.fillMaxWidth()
                    .padding(24.dp)
            )
            DesignText(
                text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vestibulum accumsan elementum commodo. Aenean tortor lorem, fringilla sit amet libero vel, sagittis gravida dolor. Ut laoreet eros neque, a ultrices elit venenatis nec. Quisque aliquam, neque ut vulputate pellentesque, purus sem gravida tortor, a tincidunt leo orci a massa. Donec eleifend ex vel neque ultricies sollicitudin.",
                maxLines = 3,
                modifier = Modifier.fillMaxWidth()
                    .padding(24.dp)
            )
        }
    }
}
