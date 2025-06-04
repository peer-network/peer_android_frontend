package eu.peernetwork.social.ui.search.tag

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.theme.PeerTheme
import eu.peernetwork.social.ui.compose.SearchItem

@Composable
fun TagItem(
    tag: String,
    onClick: (String) -> Unit = {}
) {
    val handleOnClick by rememberUpdatedState(onClick)
    SearchItem(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { handleOnClick(tag) }
            .padding(vertical = 8.dp),
        lead = {
            Box(modifier = Modifier.size(42.dp)
                .background(MaterialTheme.colorScheme.tertiaryContainer)) {
                Text(
                    text = "#",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onBackground
                    ),
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    ) {
        Text(
            text = tag,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Preview
@Composable
fun PreviewTagItem() {
    PeerTheme {
        TagItem("helloWorld") {}
    }
}
