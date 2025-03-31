package eu.peernetwork.app.ui.search

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
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
    var searchMode by remember { mutableStateOf<SearchMode?>(null) }
    val focusManager = LocalFocusManager.current
    var showResults by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }

    val usernames = remember { listOf("user1", "user2", "user3") }
    val tags = remember { listOf("android", "compose", "kotlin") }
    val titles = remember { listOf("My Post", "Hello World", "Compose Tutorial") }

    val searchHistory = remember { mutableStateListOf<Pair<SearchMode, String>>() }
    val currentList = remember(searchMode) {
        when (searchMode) {
            SearchMode.USERNAME -> usernames
            SearchMode.TAG -> tags
            SearchMode.TITLE -> titles
            null -> emptyList()
        }
    }

    LaunchedEffect(searchMode) {
        if (searchMode != null) {
            focusRequester.requestFocus()
        }
    }

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
        Box(modifier = Modifier.padding(16.dp)) {
            if (searchHistory.isNotEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 80.dp)
                        .background(
                            color = MaterialTheme.colorScheme.surface
                        )
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Recent",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.weight(1f)
                        )

                        TextButton(
                            onClick = { searchHistory.clear() },
                            colors = ButtonDefaults.textButtonColors(
                                contentColor = MaterialTheme.colorScheme.error
                            ),
                        ) {
                            Text("Clear All")
                        }
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(rememberScrollState())
                        ) {
                            searchHistory.forEachIndexed { index, (mode, term) ->
                                if (index > 0) {
                                    Spacer(modifier = Modifier.height(4.dp))
                                }

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            searchMode = mode
                                            query = term
                                        }
                                        .padding(2.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = when (mode) {
                                                SearchMode.USERNAME -> "@"
                                                SearchMode.TAG -> "#"
                                                SearchMode.TITLE -> "Title: "
                                            },
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                        Text(
                                            text = term,
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.onSurface,
                                            modifier = Modifier.padding(start = 4.dp)
                                        )
                                    }

                                    TextButton(
                                        onClick = { searchHistory.removeAt(index) },
                                        colors = ButtonDefaults.textButtonColors(
                                            contentColor = MaterialTheme.colorScheme.error
                                        ),
                                        modifier = Modifier.padding(start = 8.dp)
                                    ) {
                                        Text("Clear")
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(24.dp))
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.outlineVariant,
                        shape = RoundedCornerShape(24.dp)
                    )
            ) {
                if (searchMode == null) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        SearchMode.entries.forEach { mode ->
                            ModeSelector(
                                mode = mode,
                                onClick = {
                                    searchMode = mode
                                    focusManager.clearFocus()
                                }
                            )
                        }
                    }
                } else {
                    DesignSearchBar(
                        query = query,
                        onQueryChange = {
                            query = it
                            showResults = it.isNotEmpty()
                        },
                        onSearch = {
                            if (it.isNotEmpty()) {
                                searchHistory.add(0, searchMode!! to it)
                                showResults = false
                                focusManager.clearFocus()
                            }
                        },
                        leadingIcon = {
                            Box(
                                modifier = Modifier.clickable {
                                    searchMode = null
                                    query = ""
                                }
                            ) {
                                Text(
                                    when (searchMode) {
                                        SearchMode.USERNAME -> "@"
                                        SearchMode.TAG -> "#"
                                        SearchMode.TITLE -> "Title"
                                        null -> ""
                                    },
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            }
                        },
                        searchResults = if (showResults) currentList.filter {
                            it.contains(query, ignoreCase = true)
                        } else emptyList(),
                        onResultClick = {
                            query = it
                            searchHistory.add(0, searchMode!! to it)
                            showResults = false
                            focusManager.clearFocus()
                        },
                        shape = RoundedCornerShape(24.dp),
                        height = 54.dp,
                        elevation = 0.dp,
                        placeholder = { Text("") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(focusRequester)
                    )
                }
            }
        }
    }
}

@Composable
fun ModeSelector(
    mode: SearchMode,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val (text, icon) = when (mode) {
        SearchMode.USERNAME -> "Username" to "@"
        SearchMode.TAG -> "Tag" to "#"
        SearchMode.TITLE -> "Title" to ""
    }

    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .border(
                width = 1.dp,
                color = Color.Black,
                shape = RoundedCornerShape(16.dp)
            ),
        color = Color.White,
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (icon.isNotEmpty()) {
                Text(
                    text = icon,
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Black,
                    modifier = Modifier.padding(end = 4.dp)
                )
            }
            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Black
            )
        }
    }
}

enum class SearchMode {
    USERNAME, TAG, TITLE
}

@Preview
@Composable
fun PreviewSearchScreen() {
    PeerTheme {
        SearchContent()
    }
}
