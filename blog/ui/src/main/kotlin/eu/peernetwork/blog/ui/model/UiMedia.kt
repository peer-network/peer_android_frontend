package eu.peernetwork.blog.ui.model

import androidx.compose.runtime.Immutable

@Immutable
data class UiMedia(
    val path: String,
    val options: Options
) {
    @Immutable
    data class Options(
        val size: String,
        val resolution: Pair<Int, Int>?
    )
}
