package eu.peernetwork.media.ui.selector.video

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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

@Composable
fun VideoScreen(
    type: UiMimeType,
    directory: MutableState<String?>,
    attachment: MutableState<UiAttachment>,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner
) {
    val context = LocalContext.current
    val component = remember {
        provider.builder(Video.Builder::class.java).build(context)
    }
    val viewModel = viewModel(
        modelClass = VideoViewModel::class.java,
        viewModelStoreOwner = viewModelStoreOwner,
        factory = component.viewModelFactory()
    )
    val state by viewModel.state.collectAsStateWithLifecycle()
    val derivedState = remember {
        derivedStateOf {
            when (state) {
                VideoViewModel.State.Empty -> DesignStatefulScaffoldState.Empty
                VideoViewModel.State.Loading -> DesignStatefulScaffoldState.Loading
                is VideoViewModel.State.Success -> DesignStatefulScaffoldState.Success(
                    (state as VideoViewModel.State.Success).videos
                )
                is VideoViewModel.State.Error -> DesignStatefulScaffoldState.Error(
                    (state as VideoViewModel.State.Error).error
                )
            }
        }
    }
    val current = rememberSaveable(directory.value) { mutableStateOf(directory.value) }
    val selected = remember(attachment.value) { mutableStateOf<UiFile?>(
        if (attachment.value.media is UiMimeType.Video) {
            attachment.value.files.firstOrNull()
        } else {
            null
        }
    ) }
    val color = MaterialTheme.colorScheme.primary
    val thumbnail = viewModel.thumbnail.collectAsStateWithLifecycle().value
    DesignStatefulScaffold<List<UiFile>>(
        state = derivedState,
        onRefresh = { viewModel.initialize(directory.value) },
        contentAlignment = Alignment.TopCenter,
        modifier = Modifier.fillMaxSize()
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(4),
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(it.size) { index ->
                Box(modifier = Modifier.aspectRatio(1f)
                    .clickable(role = Role.Button) {
                        if (selected.value != null) {
                            selected.value = null
                            attachment.value = UiAttachment.File(
                                UiMimeType.Video,
                                emptyList()
                            )
                        } else {
                            selected.value = it[index]
                            attachment.value = UiAttachment.File(
                                UiMimeType.Video,
                                listOf(it[index])
                            )
                        }
                    }) {
                    DesignThumbnail(
                        it[index].thumbnail,
                        thumbnail[it[index].thumbnail]
                    ) { viewModel.thumbnail(it, type) }
                    Box(modifier = Modifier.fillMaxSize()
                        .graphicsLayer {
                            alpha = if (selected.value == it[index]) {
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
    }
    LaunchedEffect(current.value) {
        viewModel.initialize(directory.value)
    }
}
