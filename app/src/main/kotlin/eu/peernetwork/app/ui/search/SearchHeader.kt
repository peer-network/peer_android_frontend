package eu.peernetwork.app.ui.search

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.R
import eu.peernetwork.core.ui.design.compose.DesignCard
import eu.peernetwork.core.ui.design.compose.DesignTextField
import eu.peernetwork.core.ui.theme.PeerTheme

@Composable
fun SearchHeader(state: TextFieldState) {
    DesignCard(
        color = MaterialTheme.colorScheme.surfaceContainerLow,
        contentPadding = PaddingValues(vertical = 0.dp),
        trailing = {
            IconButton(onClick = {}) {
                Icon(
                    painter = painterResource(R.drawable.ic_search),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        },
        modifier = Modifier.padding(horizontal = 24.dp)
    ) {
        DesignTextField(state)
    }
}

@Preview
@Composable
fun PreviewSearchHeader() {
    PeerTheme {
        val state = remember { TextFieldState() }
        SearchHeader(state)
    }
}
