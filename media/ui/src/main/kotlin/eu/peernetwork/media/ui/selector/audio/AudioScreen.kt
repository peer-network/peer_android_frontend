package eu.peernetwork.media.ui.selector.audio

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
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
import eu.peernetwork.media.ui.R
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList

@Composable
fun AudioScreen(
    type: UiMimeType,
    directory: MutableState<String?>,
    attachment: State<UiAttachment>,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    onSelect: (UiAttachment) -> Unit
) {
    val context = LocalContext.current
    val component = remember {
        provider.builder(Audio.Builder::class.java).build(context)
    }
    val viewModel = viewModel(
        modelClass = AudioViewModel::class.java,
        viewModelStoreOwner = viewModelStoreOwner,
        factory = component.viewModelFactory()
    )
    val state by viewModel.state.collectAsStateWithLifecycle()
    val derivedState = remember {
        derivedStateOf {
            when (state) {
                AudioViewModel.State.Empty -> DesignStatefulScaffoldState.Empty
                AudioViewModel.State.Loading -> DesignStatefulScaffoldState.Loading
                is AudioViewModel.State.Success -> DesignStatefulScaffoldState.Success(
                    (state as AudioViewModel.State.Success).audios
                )
                is AudioViewModel.State.Error -> DesignStatefulScaffoldState.Error(
                    (state as AudioViewModel.State.Error).error
                )
            }
        }
    }
    val color = MaterialTheme.colorScheme.primary
    val current = rememberSaveable(directory.value) { mutableStateOf(directory.value) }
    val selected = remember { mutableStateOf<UiAttachment>(attachment.value) }
    val listState = rememberLazyGridState()
    val canLoad = remember {
        derivedStateOf {
            listState.layoutInfo.totalItemsCount > 0 && !listState.isScrollInProgress
        }
    }
    val handleSelect by rememberUpdatedState(onSelect)
    DesignStatefulScaffold<List<UiFile>>(
        state = derivedState,
        onRefresh = { viewModel.initialize(directory.value) },
        contentAlignment = Alignment.TopCenter,
        modifier = Modifier.fillMaxSize()
    ) {
        LazyVerticalGrid(
            state = listState,
            columns = GridCells.Fixed(4),
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(it.size) { index ->
                val audioFile = it[index]
                val isSelected = selected.value.files.contains(audioFile)
                Box(
                    modifier = Modifier
                        .aspectRatio(1f)
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .clickable(role = Role.Button) {
                            selected.value = if (isSelected) {
                                UiAttachment.File(type, persistentListOf())
                            } else {
                                UiAttachment.File(type, persistentListOf(audioFile))
                            }
                            handleSelect(selected.value)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .graphicsLayer { alpha = if (isSelected) 1f else 0f }
                                .drawBehind {
                                    drawRoundRect(
                                        color = color,
                                        size = size,
                                        style = Stroke(width = 4.dp.toPx())
                                    )
                                }
                        )

                        Column(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .padding(8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Image(
                                painter = painterResource(id = eu.peernetwork.core.ui.R.drawable.ic_music),
                                contentDescription = "Audio Icon",
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(8.dp)
                                    .fillMaxSize(),
                                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
                            )

                            Text(
                                text = audioFile.props?.getString("name") ?: audioFile.name,
                                style = MaterialTheme.typography.bodySmall,
                                maxLines = 1,
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
        }
        LaunchedEffect(canLoad.value) {
            if (canLoad.value) {
                viewModel.sync(
                    type,
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
