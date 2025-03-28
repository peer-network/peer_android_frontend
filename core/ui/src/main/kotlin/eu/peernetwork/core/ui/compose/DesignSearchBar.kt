package eu.peernetwork.core.ui.compose

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.theme.PeerTheme

@OptIn(ExperimentalAnimationApi::class, ExperimentalFoundationApi::class)
@Composable
fun DesignSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    searchResults: List<String>,
    onResultClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    shape: Shape = MaterialTheme.shapes.extraLarge,
    backgroundColor: Color = MaterialTheme.colorScheme.surface,
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
    placeholder: @Composable () -> Unit = { Text("Search...") },
    leadingIcon: @Composable (() -> Unit)? = { Icon(Icons.Default.Search, null) },
    trailingIcon: @Composable (() -> Unit)? = null,
    resultItem: @Composable (String) -> Unit = { result ->
        Text(
            text = result,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
    },
    enterTransition: EnterTransition = fadeIn(),
    exitTransition: ExitTransition = fadeOut(),
    elevation: Dp = 6.dp,
    height: Dp = 56.dp,
    horizontalPadding: Dp = 16.dp
) {
    var active by rememberSaveable { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    val interactionSource = remember { MutableInteractionSource() }
    val clickModifier = if (active) {
        Modifier
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) {
                focusManager.clearFocus()
                active = false
            }
    } else {
        Modifier
    }
    val enterTransition = fadeIn(animationSpec = tween(300)) +
            expandVertically(
                animationSpec = tween(300),
                expandFrom = Alignment.Top
            )

    val exitTransition = fadeOut(animationSpec = tween(250)) +
            shrinkVertically(
                animationSpec = tween(250),
                shrinkTowards = Alignment.Top
            )

    Box(
        modifier = modifier
            .then(clickModifier)
            .fillMaxWidth()
            .shadow(elevation, shape)
            .background(backgroundColor, shape)
            .clip(shape)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(height)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = {}
                    )
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = horizontalPadding)
                        .align(Alignment.CenterStart)
                ) {
                    leadingIcon?.invoke()

                    Spacer(modifier = Modifier.width(8.dp))

                    Box(
                        modifier = Modifier.weight(1f)
                    ) {
                        if (query.isEmpty() && !active) {
                            placeholder()
                        }
                        BasicTextField(
                            value = query,
                            onValueChange = {
                                onQueryChange(it)
                            },
                            textStyle = LocalTextStyle.current.copy(color = contentColor),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                            keyboardActions = KeyboardActions(
                                onSearch = { onSearch(query) }
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .onFocusChanged { active = it.isFocused }
                        )
                    }

                    AnimatedVisibility(
                        visible = query.isNotEmpty(),
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        IconButton(
                            onClick = {
                                onQueryChange("")
                            }
                        ) {
                            Icon(Icons.Default.Close, "Clear")
                        }
                    }

                    trailingIcon?.invoke()
                }
            }

            AnimatedVisibility(
                visible = active && searchResults.isNotEmpty(),
                enter = enterTransition,
                exit = exitTransition
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.9f))
                        .clip(RoundedCornerShape(bottomStart = 12.dp, bottomEnd = 12.dp))
                ) {
                    items(
                        items = searchResults,
                        key = { it }
                    ) { result ->
                        var itemVisible by remember { mutableStateOf(false) }

                        LaunchedEffect(Unit) {
                            itemVisible = true
                        }

                        AnimatedVisibility(
                            visible = itemVisible,
                            enter = fadeIn(animationSpec = tween(100)) +
                                    slideInVertically(animationSpec = tween(150)),
                            exit = fadeOut(animationSpec = tween(100))
                        ) {
                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        onResultClick(result)
                                        active = false
                                    }
                                    .animateItemPlacement(),
                                color = Color.Transparent
                            ) {
                                resultItem(result)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
@Preview
fun PreviewDesignSearchBar() {
    PeerTheme {
        var query by remember { mutableStateOf("") }
        val results = remember {
            listOf("Android", "Kotlin", "Compose", "Material Design")
        }
        Box(modifier = Modifier.padding(16.dp)) {
            DesignSearchBar(
                query = query,
                onQueryChange = { query = it },
                onSearch = { println("Search: $it") },
                searchResults = results.filter { it.contains(query, ignoreCase = true) },
                onResultClick = { query = it },
                shape = RoundedCornerShape(24.dp),
                height = 48.dp,
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
