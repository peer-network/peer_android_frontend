package eu.peernetwork.core.ui.usecase

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import eu.peernetwork.core.common.usecase.ParameterizedBlockingUseCase
import eu.peernetwork.core.ui.theme.PrimaryColor
import javax.inject.Inject

class AnnotationUsecase @Inject constructor() : ParameterizedBlockingUseCase<String, AnnotatedString> {
    override fun invoke(param: String): AnnotatedString {
        val pattern = Regex("""(@\w+)|(#\w+)|((https?|ftp)://[^\s]+)|(www\.[^\s]+)""", RegexOption.IGNORE_CASE)
        return buildAnnotatedString {
            val matches = pattern.findAll(param)
            var lastIndex = 0
            matches.forEach { match ->
                val value = match.value
                append(param.substring(lastIndex, match.range.first))
                val annotationTag = when {
                    value.startsWith("http", true) || value.startsWith("ftp", true) || value.startsWith("www.", true) -> "URL"
                    value.startsWith("@") -> "MENTION"
                    value.startsWith("#") -> "HASHTAG"
                    else -> "PLAIN"
                }
                val annotationValue = if (value.startsWith("www.", true)) "https://$value" else value
                pushStringAnnotation(tag = annotationTag, annotation = annotationValue)
                if (annotationTag != "PLAIN") {
                    withStyle(style = SpanStyle(color = PrimaryColor)) {
                        append(value)
                    }
                } else {
                    append(value)
                }
                pop()
                lastIndex = match.range.last + 1
            }
            if (lastIndex < param.length) {
                append(param.substring(lastIndex))
            }
        }
    }
}
