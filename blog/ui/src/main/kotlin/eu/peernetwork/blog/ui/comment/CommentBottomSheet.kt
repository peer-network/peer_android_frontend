package eu.peernetwork.blog.ui.comment

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import eu.peernetwork.blog.ui.model.UiComment
import eu.peernetwork.core.common.model.Pageable
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.component.DesignStatefulContent
import eu.peernetwork.core.ui.design.component.DesignStatefulContentState
import eu.peernetwork.core.ui.design.compose.DesignBottomSheet
import eu.peernetwork.core.ui.extension.builder
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommentBottomSheet(
    state: MutableState<String?>,
    postLimit: Int,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val component = remember { provider.builder(Comment.Builder::class.java).build(context) }
    val viewModel = viewModel(
        modelClass = CommentViewModel::class.java,
        viewModelStoreOwner = viewModelStoreOwner,
        factory = component.viewModelFactory()
    )
    val sheetState = viewModel.state.collectAsStateWithLifecycle()
    val derivedState = remember {
        derivedStateOf {
            when (sheetState.value) {
                CommentViewModel.State.Empty -> DesignStatefulContentState.Empty
                CommentViewModel.State.Loading -> DesignStatefulContentState.Loading
                is CommentViewModel.State.Success -> {
                    DesignStatefulContentState.Success(
                        (sheetState.value as CommentViewModel.State.Success).content
                    )
                }
                is CommentViewModel.State.Error -> DesignStatefulContentState.Error(
                    (sheetState.value as CommentViewModel.State.Error).error
                )
            }
        }
    }
    val currentTime = remember { mutableLongStateOf(System.currentTimeMillis()) }
    LaunchedEffect(Unit) {
        while (true) {
            delay(60_000L)
            currentTime.longValue = System.currentTimeMillis()
        }
    }
    val showSheet = remember(state.value) { mutableStateOf(state.value != null) }
    DesignBottomSheet(
        showSheet = showSheet,
        tag = "commentBottomSheet",
        modifier = modifier,
        onDismissRequest = { state.value = null },
        color = MaterialTheme.colorScheme.tertiaryContainer,
        sheetPeekHeight = 400.dp,
        content = {
            DesignStatefulContent<Flow<PagingData<UiComment>>>(
                state = derivedState,
                onRefresh = { state.value?.let { viewModel.load(it, Pageable(0, postLimit)) } },
                modifier = Modifier.height(400.dp)
            ) { flow ->
                val items = flow.collectAsLazyPagingItems()
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(items.itemCount) { index ->
                        items[index]?.let { comment ->
                            Row(
                                modifier = modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalAlignment = Alignment.Top,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row {
                                    Text(
                                        text = comment.author.username,
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Text(
                                        text = comment.content,
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                            }
                        }
                    }
                }
            }
            LaunchedEffect(state.value) {
                state.value?.let { viewModel.load(it, Pageable(0, postLimit)) }
            }
        }
    )
}
