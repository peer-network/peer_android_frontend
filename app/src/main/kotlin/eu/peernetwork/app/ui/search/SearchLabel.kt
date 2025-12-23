package eu.peernetwork.app.ui.search

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.app.mapper.label
import eu.peernetwork.app.mapper.symbol
import eu.peernetwork.core.ui.theme.DesignTheme

@Composable
fun SearchLabel(
    modifier: Modifier = Modifier,
    onClick: (SearchMode) -> Unit,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        SearchLabel(SearchMode.Username, onClick = onClick)
        SearchLabel(SearchMode.Tag, onClick = onClick)
        SearchLabel(SearchMode.Title, onClick = onClick)
    }
}

@Composable
fun SearchLabel(mode: SearchMode, onClick: (SearchMode) -> Unit) {
    Text(
        text = "${mode.symbol}${mode.label()}",
        style = MaterialTheme.typography.bodyMedium.copy(
            color = MaterialTheme.colorScheme.outline
        ),
        modifier = Modifier
            .padding(vertical = 8.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.surfaceContainerLow)
            .clickable(role = Role.Button, onClick = { onClick(mode) })
            .padding(vertical = 8.dp)
            .padding(horizontal = 12.dp)
    )
}

@Preview
@Composable
fun PreviewSearchLabel() {
    DesignTheme {
        SearchLabel {}
    }
}
