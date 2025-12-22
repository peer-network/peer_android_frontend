package eu.peernetwork.core.ui.design.luna

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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

sealed class DesignRichText(val value: String) {
    object Link : DesignRichText("*")
    object Mention : DesignRichText("@")
    object Tag : DesignRichText("#")
}

@Composable
fun DesignRichText(
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
    onTap: () -> Unit = { },
    onClick: (DesignRichText, String) -> Unit = { _,_ -> },
    onTextLayout: (TextLayoutResult) -> Unit = {},
    style: TextStyle = LocalTextStyle.current
) {
    var layoutResult by remember { mutableStateOf<TextLayoutResult?>(null) }
    val handleOnTextLayout by rememberUpdatedState(onTextLayout)
    val handleOnTap by rememberUpdatedState(onTap)
    DesignText(
        text = text,
        modifier = modifier,
        onTap = { offset ->
            layoutResult?.let { layout ->
                val position = layout.getOffsetForPosition(offset)
                val hasAnnotation = text.hasStringAnnotations(
                    start = position,
                    end = position,
                    onClick = onClick
                )
                if (!hasAnnotation) {
                    handleOnTap()
                }
            } ?: handleOnTap()
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

fun AnnotatedString.hasStringAnnotations(
    start: Int,
    end: Int,
    onClick: (DesignRichText, String) -> Unit
): Boolean {
    var found = false
    listOf(DesignRichText.Tag, DesignRichText.Mention, DesignRichText.Link).forEach { type ->
        getStringAnnotations(tag = type.value, start = start, end = end)
            .firstOrNull()
            ?.let { annotation ->
                onClick(type, annotation.item.trimStart(type.value[0]))
                found = true
            }
    }
    return found
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
            DesignRichText(
                text = annotatedString,
                maxLines = 1,
                onClick = { type, value ->
                    when (type) {
                        DesignRichText.Tag -> {
                            count.longValue = System.currentTimeMillis()
                        }
                        DesignRichText.Mention -> {
                            count.longValue = System.currentTimeMillis() / 10000
                        }
                        DesignRichText.Link -> {
                            count.longValue = -System.currentTimeMillis()
                        }
                    }
                }
            )
        }
    }
}
