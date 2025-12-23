package eu.peernetwork.core.ui.mapper

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import eu.peernetwork.core.ui.design.luna.DesignRichText
import eu.peernetwork.core.ui.theme.PrimaryColor

fun String.annotate(color: Color = PrimaryColor): AnnotatedString {
    val combinedPattern = Regex("""(#\w+)|(@\w+)|(\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Z|a-z]{2,}\b)|(https?://[^\s]+)|(www\.[^\s]+)""")
    return buildAnnotatedString {
        append(this@annotate)
        combinedPattern.findAll(this@annotate).forEach { match ->
            val start = match.range.first
            val end = match.range.last + 1
            when {
                match.value.startsWith("#") -> {
                    addStyle(SpanStyle(color = color), start, end)
                    addStringAnnotation(
                        tag = DesignRichText.Tag.value,
                        annotation = match.value,
                        start = start,
                        end = end
                    )
                }
                match.value.startsWith("@") -> {
                    addStyle(SpanStyle(color = color), start, end)
                    addStringAnnotation(
                        tag = DesignRichText.Mention.value,
                        annotation = match.value,
                        start = start,
                        end = end
                    )
                }
                else -> {
                    addStyle(SpanStyle(color = color), start, end)
                    addStringAnnotation(
                        tag = DesignRichText.Link.value,
                        annotation = match.value,
                        start = start,
                        end = end
                    )
                }
            }
        }
    }
}
