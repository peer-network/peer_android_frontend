package eu.peernetwork.media.ui.selector.directory

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import eu.peernetwork.core.ui.R
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.compose.DesignStatefulScaffold
import eu.peernetwork.core.ui.design.compose.DesignStatefulScaffoldState
import eu.peernetwork.core.ui.design.material.DesignThumbnail
import eu.peernetwork.core.ui.extension.builder
import eu.peernetwork.media.core.model.UiMimeType
import eu.peernetwork.media.core.model.UiDirectory

@Composable
fun DirectoryScreen(
    type: UiMimeType,
    onSelect: (String?) -> Unit,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner
) {
    val context = LocalContext.current
    val component = remember {
        provider.builder(Directory.Builder::class.java).build(context)
    }
    val viewModel = viewModel(
        modelClass = DirectoryViewModel::class.java,
        viewModelStoreOwner = viewModelStoreOwner,
        factory = component.viewModelFactory()
    )
    val state by viewModel.state.collectAsStateWithLifecycle()
    val derivedState = remember {
        derivedStateOf {
            when (state) {
                DirectoryViewModel.State.Empty -> DesignStatefulScaffoldState.Empty
                DirectoryViewModel.State.Loading -> DesignStatefulScaffoldState.Loading
                is DirectoryViewModel.State.Success -> DesignStatefulScaffoldState.Success(
                    (state as DirectoryViewModel.State.Success).directories
                )
                is DirectoryViewModel.State.Error -> DesignStatefulScaffoldState.Error(
                    (state as DirectoryViewModel.State.Error).error
                )
            }
        }
    }
    val thumbnail = viewModel.thumbnail.collectAsStateWithLifecycle()
    val listState = rememberLazyGridState()
    val enable = remember { derivedStateOf { !listState.isScrollInProgress } }
    DesignStatefulScaffold<Set<UiDirectory>>(
        state = derivedState,
        onRefresh = { viewModel.initialize(type) },
        contentAlignment = Alignment.TopCenter,
        modifier = Modifier.fillMaxSize()
    ) {
        LazyVerticalGrid(
            state = listState,
            columns = GridCells.Fixed(3),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.padding(horizontal = 18.dp)
                .padding(top = 8.dp)
        ) {
            items(it.size) { index ->
                val item = it.elementAt(index)
                val bitmap = remember { derivedStateOf { thumbnail.value[item.thumbnail] } }
                key(item.name) {
                    Box(
                        contentAlignment = Alignment.BottomStart,
                        modifier = Modifier.aspectRatio(1f)
                            .clip(RoundedCornerShape(24.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .clickable(role = Role.Button) {
                                onSelect(item.path)
                            }
                    ) {
                        DesignThumbnail(
                            enable = enable,
                            thumbnail = item.thumbnail,
                            bitmap = bitmap,
                            modifier = Modifier.fillMaxWidth()
                                .aspectRatio(1f)
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                        ) { media -> viewModel.mediaThumbnail(media, type) }
                        Image(
                            painter = painterResource(R.drawable.overlay_gradient),
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                        Text(
                            item.name,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = MaterialTheme.colorScheme.tertiary
                            ),
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                }
            }
            item(span = { GridItemSpan(maxLineSpan) }) {
                Box(modifier = Modifier.height(56.dp))
            }
        }
    }
    LaunchedEffect(type) {
        viewModel.initialize(type)
    }
    DisposableEffect(Unit) {
        onDispose { viewModel.reset() }
    }
}
