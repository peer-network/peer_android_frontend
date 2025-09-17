package eu.peernetwork.media.ui.selector.photo

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.component.DesignStatefulScaffold
import eu.peernetwork.core.ui.design.component.DesignStatefulScaffoldState
import eu.peernetwork.core.ui.design.compose.DesignCollapsibleBottomSheet
import eu.peernetwork.core.ui.design.compose.DesignOutlinedButton
import eu.peernetwork.core.ui.design.compose.DesignTitle
import eu.peernetwork.core.ui.design.compose.DesignTitleBarHost
import eu.peernetwork.core.ui.extension.builder
import eu.peernetwork.core.ui.factory.UiViewModelStore
import eu.peernetwork.media.core.model.UiFile
import eu.peernetwork.media.core.model.UiMimeType
import eu.peernetwork.media.ui.R
import eu.peernetwork.media.ui.compose.ThumbnailPlaceholder
import eu.peernetwork.media.ui.selector.directory.DirectoryScreen

@Composable
fun PhotoPage(
    attachment: UiFile,
    provider: UiComponentProvider,
    onSelect: (UiFile) -> Unit,
) {
    val context = LocalContext.current
    val component = remember {
        provider.builder(Photo.Builder::class.java).build(context)
    }
    val viewModelStore = remember { UiViewModelStore.Delegate() }
    val directory = rememberSaveable { mutableStateOf<String?>(null) }
    val tag = directory.value ?: UiMimeType.Photo.id.toString()
    val viewModel = viewModel(
        modelClass = PhotoViewModel::class.java,
        viewModelStoreOwner = viewModelStore.get(tag),
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
    val thumbnail = viewModel.thumbnail.collectAsStateWithLifecycle()
    val listState = rememberLazyGridState()
    val showDirectory = rememberSaveable { mutableStateOf(false) }
    val enable = remember { derivedStateOf { !listState.isScrollInProgress } }
    val handleOnSelect by rememberUpdatedState(onSelect)
    PhotoPage(onShowDirectory = { showDirectory.value = true }) {
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
                    val selected = attachment.copy(cover = it[index].uri)
                    val bitmap = remember { derivedStateOf { thumbnail.value[it[index].path] } }
                    PhotoItem(
                        isSelected = it[index] == selected,
                        color = color,
                        enable = enable,
                        thumbnail = it[index].path,
                        bitmap = bitmap,
                        onSelect = { handleOnSelect(selected) }
                    ) { media -> viewModel.mediaThumbnail(media, UiMimeType.Photo) }
                }
            }
        }
        DesignCollapsibleBottomSheet(
            state = showDirectory,
            peekHeight = 400.dp,
            onDismiss = { showDirectory.value = false }
        ) {
            Box(modifier = Modifier.statusBarsPadding()) {
                DirectoryScreen(
                    type = UiMimeType.Photo,
                    onSelect = {
                        directory.value = it
                        showDirectory.value = false },
                    provider = component,
                    viewModelStoreOwner = viewModelStore.get(tag),
                )
            }
        }
    }
    DesignTitleBarHost("ExplorerScreen") {
        titleBar {
            DesignTitle {
                Text(stringResource(R.string.gallery))
            }
        }
    }
    LaunchedEffect(directory.value) {
        viewModel.initialize(directory.value)
    }
    DisposableEffect(Unit) {
        onDispose { viewModel.reset() }
    }
}

@Composable
fun PhotoPage(
    onShowDirectory: () -> Unit,
    content: @Composable () -> Unit
) {
    val color = MaterialTheme.colorScheme.surfaceVariant
    val border = MaterialTheme.colorScheme.surfaceVariant
    val updatedContent by rememberUpdatedState(content)
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .drawBehind {
                    val strokeWidth = 1.dp.toPx()
                    val y = size.height - strokeWidth / 2
                    drawLine(
                        color = border,
                        start = Offset(0f, y),
                        end = Offset(size.width, y),
                        strokeWidth = strokeWidth
                    )
                }
                .padding(vertical = 12.dp, horizontal = 24.dp)) {
            DesignOutlinedButton(
                onClick = onShowDirectory,
                modifier = Modifier.background(
                    color = MaterialTheme.colorScheme.onBackground,
                    shape = RoundedCornerShape(28),
                ),
                enabled = true,
                shape = RoundedCornerShape(28),
                textStyle = MaterialTheme.typography.labelLarge.copy(
                    color = color,
                    fontWeight = FontWeight.SemiBold
                ),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = Color.Transparent,
                    contentColor = color,
                    disabledContainerColor = Color.Transparent
                ),
                minHeight = 32.dp,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.onBackground),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp)
            ) { Text(stringResource(R.string.browse_label)) }
        }
        updatedContent()
    }
}
