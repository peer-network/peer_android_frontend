package eu.peernetwork.app.mapper

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import eu.peernetwork.app.R
import eu.peernetwork.app.ui.search.SearchMode

val SearchMode.symbol: String get() = when(this) {
    is SearchMode.Default -> "@"
    is SearchMode.Username -> "@"
    is SearchMode.Tag -> "#"
    is SearchMode.Title -> "~"
}

@Composable
fun SearchMode.label() = when(this) {
    is SearchMode.Default -> stringResource(R.string.mention_label)
    is SearchMode.Username -> stringResource(R.string.mention_label)
    is SearchMode.Tag -> stringResource(R.string.tag_label)
    is SearchMode.Title -> stringResource(R.string.title_label)
}
