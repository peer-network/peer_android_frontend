package eu.peernetwork.core.ui.extension

import androidx.compose.ui.text.AnnotatedString

fun AnnotatedString.sliceWithAnnotations(start: Int, end: Int): AnnotatedString {
    if (start >= end || start < 0 || end > this.length) return AnnotatedString("")
    val subText = this.text.substring(start, end)
    val builder = AnnotatedString.Builder(subText)
    this.spanStyles.filter { style -> style.start < end && style.end > start }.forEach { style ->
        val sliceStart = (style.start - start).coerceAtLeast(0)
        val sliceEnd = (style.end - start).coerceAtMost(subText.length)
        if (sliceStart < sliceEnd) builder.addStyle(style.item, sliceStart, sliceEnd)
    }
    this.getStringAnnotations(start, end).forEach { annotation ->
        val sliceStart = (annotation.start - start).coerceAtLeast(0)
        val sliceEnd = (annotation.end - start).coerceAtMost(subText.length)
        if (sliceStart < sliceEnd) builder.addStringAnnotation(
            annotation.tag,
            annotation.item,
            sliceStart,
            sliceEnd
        )
    }
    return builder.toAnnotatedString()
}