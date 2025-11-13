package eu.peernetwork.core.ui.design.luna

import android.content.res.Configuration
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import eu.peernetwork.core.ui.mapper.annotate
import eu.peernetwork.core.ui.theme.DesignTheme

enum class DesignStyledTextType(val value: String) {
    LINK("*"),
    MENTION("@"),
    TAG("#")
}

@Composable
fun DesignStyledText(
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
    inlineContent: Map<String, InlineTextContent> = mapOf(),
    onClick: (DesignStyledTextType) -> Unit = {},
    onTextLayout: (TextLayoutResult) -> Unit = {},
    style: TextStyle = LocalTextStyle.current
) {
    var layoutResult: TextLayoutResult? = null
    val handleOnTextLayout by rememberUpdatedState(onTextLayout)
    DesignText(
        text = text,
        modifier = Modifier
            .pointerInput(Unit) {
                detectTapGestures { offset ->
                    layoutResult?.let { layout ->
                        val position = layout.getOffsetForPosition(offset)
                        text.getStringAnnotations(
                            start = position,
                            end = position,
                            onClick = onClick
                        )
                    }
                }
            }.then(modifier),
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
        onTextLayout = {
            layoutResult = it
            handleOnTextLayout(it)
        },
        style = style
    )
}

fun AnnotatedString.getStringAnnotations(
    start: Int,
    end: Int,
    onClick: (DesignStyledTextType) -> Unit
) {
    listOf(
        DesignStyledTextType.LINK,
        DesignStyledTextType.MENTION,
        DesignStyledTextType.TAG,
    ).forEach {
        getStringAnnotations(
            tag = it.value,
            start = start,
            end = end
        ).firstOrNull()?.let { _ ->
            onClick(it)
        }
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun DesignStyledTextPreview() {
    DesignTheme {
        val count = remember { mutableLongStateOf(System.currentTimeMillis()) }
        val annotatedString = """
            Hello, #world from @peer www.localhost #oneLove test@domain.de
        """.trimIndent()
            .annotate()
        Column {
            Text(text = count.longValue.toString())
            DesignStyledText(
                text = annotatedString,
                onClick = {
                    when (it) {
                        DesignStyledTextType.TAG -> {
                            count.longValue = System.currentTimeMillis()
                        }
                        DesignStyledTextType.MENTION -> {
                            count.longValue = System.currentTimeMillis() / 10000
                        }
                        DesignStyledTextType.LINK -> {
                            count.longValue = -System.currentTimeMillis()
                        }
                    }
                }
            )
        }
    }
}
