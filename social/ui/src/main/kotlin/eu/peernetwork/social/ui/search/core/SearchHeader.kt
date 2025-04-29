package eu.peernetwork.social.ui.search.core

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.R
import eu.peernetwork.core.ui.design.compose.DesignCard
import eu.peernetwork.core.ui.theme.PeerTheme

@Composable
fun SearchHeader() {
    DesignCard(
        contentPadding = PaddingValues(vertical = 0.dp),
        trailing = {
            IconButton(onClick = {}) {
                Icon(
                    painter = painterResource(R.drawable.ic_search),
                    contentDescription = null
                )
            }
        },
        modifier = Modifier.padding(horizontal = 24.dp)
    ) {

    }
}

@Preview
@Composable
fun PreviewSearchHeader() {
    PeerTheme {
        SearchHeader()
    }
}
