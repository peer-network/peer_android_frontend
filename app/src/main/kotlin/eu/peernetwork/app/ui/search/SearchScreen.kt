package eu.peernetwork.app.ui.search

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import eu.peernetwork.core.ui.R
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.compose.DesignSearchBar
import eu.peernetwork.core.ui.compose.DesignToolbarTitle
import eu.peernetwork.core.ui.theme.PeerTheme

@Composable
fun SearchScreen(
    title: MutableState<DesignToolbarTitle>,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
) {
    SearchContent()
    LaunchedEffect(Unit) {
        title.value = DesignToolbarTitle(R.string.search_label)
    }
}

@Composable
fun SearchContent() {
    var query by remember { mutableStateOf("") }
    val results = remember {
        listOf("Android", "Kotlin", "Compose", "Material Design")
    }
    val focusManager = LocalFocusManager.current
    var showResults by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                showResults = false
                focusManager.clearFocus()
            }
    ) {
        Box(
            modifier = Modifier
                .padding(16.dp)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = {}
                )
        ) {
            DesignSearchBar(
                query = query,
                onQueryChange = {
                    query = it
                    showResults = it.isNotEmpty()
                },
                onSearch = {
                    println("Search: $it")
                    showResults = false
                },
                leadingIcon = {
                    Text("@")
                },
                searchResults = if (showResults) results.filter {
                    it.contains(query, ignoreCase = true)
                } else emptyList(),
                onResultClick = {
                    query = it
                    showResults = false
                    focusManager.clearFocus()
                },
                shape = RoundedCornerShape(24.dp),
                height = 56.dp,
                resultItem = { result ->
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = result,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            )
        }
    }
}

@Preview
@Composable
fun PreviewSearchScreen() {
    PeerTheme {
        SearchContent()
    }
}
