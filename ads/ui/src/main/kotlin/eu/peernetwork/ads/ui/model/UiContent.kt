package eu.peernetwork.ads.ui.model

import androidx.compose.ui.text.AnnotatedString

data class UiContent(
    val title: AnnotatedString,
    val description: AnnotatedString,
    val image: String?
)
