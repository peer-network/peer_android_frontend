package eu.peernetwork.media.ui.selector.photo

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.component.DesignStatefulScaffold
import eu.peernetwork.core.ui.design.component.DesignStatefulScaffoldState
import eu.peernetwork.core.ui.extension.builder
import eu.peernetwork.media.core.model.UiAttachment
import eu.peernetwork.media.core.model.UiFile
import eu.peernetwork.media.core.model.UiMimeType
import eu.peernetwork.core.ui.design.compose.DesignThumbnail
import eu.peernetwork.media.ui.compose.ThumbnailPlaceholder
import kotlinx.collections.immutable.toPersistentList

@Composable
fun PhotoScreen(
    directory: MutableState<String?>,
    attachment: State<UiAttachment>,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    onSelect: (UiAttachment) -> Unit
) {
    val context = LocalContext.current
    val component = remember {
        provider.builder(Photo.Builder::class.java).build(context)
    }
    val viewModel = viewModel(
        modelClass = PhotoViewModel::class.java,
        viewModelStoreOwner = viewModelStoreOwner,
        factory = component.viewModelFactory()
    )
    val state by viewModel.state.collectAsStateWithLifecycle()
    val derivedState = remember {
        derivedStateOf {
            when (state) {
                PhotoViewModel.State.Empty -> DesignStatefulScaffoldState.Empty
                PhotoViewModel.State.Loading -> DesignStatefulScaffoldState.Loading
                is PhotoViewModel.State.Success -> DesignStatefulScaffoldState.Success(
                    (state as PhotoViewModel.State.Success).photos
                )
                is PhotoViewModel.State.Error -> DesignStatefulScaffoldState.Error(
                    (state as PhotoViewModel.State.Error).error
                )
            }
        }
    }
    val color = MaterialTheme.colorScheme.primary
    val current = rememberSaveable(directory.value) { mutableStateOf(directory.value) }
    val selected = remember { mutableStateOf<UiAttachment>(attachment.value) }
    val thumbnail = viewModel.thumbnail.collectAsStateWithLifecycle()
    val listState = rememberLazyGridState()
    val canLoad = remember { derivedStateOf {
        listState.layoutInfo.totalItemsCount > 0 && !listState.isScrollInProgress
    } }
    val handleSelect by rememberUpdatedState(onSelect)
    DesignStatefulScaffold<List<UiFile>>(
        state = derivedState,
        onRefresh = { viewModel.initialize(directory.value) },
        contentAlignment = Alignment.TopCenter,
        modifier = Modifier.fillMaxSize(),
        placeholder = { ThumbnailPlaceholder(modifier = Modifier.fillMaxSize()) }
    ) {
        LazyVerticalGrid(
            state = listState,
            columns = GridCells.Fixed(4),
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(it.size) { index ->
                val isSelected = selected.value.files.contains(it[index])
                Box(modifier = Modifier
                    .aspectRatio(1f)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .clickable(role = Role.Button) {
                        selected.value = if (isSelected) {
                            UiAttachment.File(
                                UiMimeType.Photo,
                                selected.value.files.filterNot { file ->
                                    file.uri == it[index].uri
                                }.toPersistentList()
                            )
                        } else {
                            UiAttachment.File(
                                UiMimeType.Photo,
                                (selected.value.files + it[index]).toPersistentList()
                            )
                        }
                        handleSelect(selected.value)
                    }) {
                    DesignThumbnail(
                        it[index].thumbnail,
                        thumbnail.value[it[index].thumbnail]
                    ) {  }
                    Box(modifier = Modifier
                        .fillMaxSize()
                        .graphicsLayer {
                            alpha = if (isSelected) {
                                1f
                            } else {
                                0f
                            }
                        }.drawBehind {
                            drawRoundRect(
                                color = color,
                                size = size,
                                style = Stroke(width = 4.dp.toPx())
                            )
                        }
                    )
                }
            }
        }
        LaunchedEffect(canLoad.value, directory.value) {
            if (canLoad.value) {
                viewModel.sync(
                    UiMimeType.Photo,
                    listState.firstVisibleItemIndex,
                    listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
                )
            }
        }
    }
    LaunchedEffect(current.value) {
        viewModel.initialize(directory.value)
    }
}
