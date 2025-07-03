package eu.peernetwork.social.ui.block

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import eu.peernetwork.core.common.model.Pageable
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.component.DesignErrorLabel
import eu.peernetwork.core.ui.design.component.DesignPagingScaffold
import eu.peernetwork.core.ui.design.component.DesignRefreshableScaffold
import eu.peernetwork.core.ui.design.component.DesignStatefulScaffoldState
import eu.peernetwork.core.ui.design.compose.DesignTitle
import eu.peernetwork.core.ui.design.compose.DesignTitleBarHost
import eu.peernetwork.core.ui.extension.builder
import eu.peernetwork.social.ui.R
import eu.peernetwork.social.ui.compose.Peer
import eu.peernetwork.social.ui.compose.SearchItemSkeleton
import eu.peernetwork.social.ui.model.UiBlock
import eu.peernetwork.social.ui.model.UiMember

@Composable
fun BlockScreen(
    userId: String,
    postLimit: Int,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    onClick: (UiMember) -> Unit
) {
    val context = LocalContext.current
    val component = remember {
        provider.builder(Block.Builder::class.java).build(context)
    }
    val viewModel: BlockViewModel = viewModel(
        viewModelStoreOwner = viewModelStoreOwner,
        factory = component.viewModelFactory()
    )
    val state by viewModel.state.collectAsState()
    val derivedState = remember {
        derivedStateOf {
            when (state) {
                BlockViewModel.State.Empty -> DesignStatefulScaffoldState.Empty
                BlockViewModel.State.Loading -> DesignStatefulScaffoldState.Loading
                is BlockViewModel.State.Success -> {
                    DesignStatefulScaffoldState.Success(
                        (state as BlockViewModel.State.Success).content
                    )
                }
                is BlockViewModel.State.Error -> DesignStatefulScaffoldState.Error(
                    (state as BlockViewModel.State.Error).error
                )
            }
        }
    }
    val pageState = remember { mutableStateOf(DesignStatefulScaffoldState.Success(Unit)) }
    DesignRefreshableScaffold<Unit>(
        state = pageState,
        onRefresh = { viewModel.blockList(userId, Pageable(0, postLimit)) }
    ) {
        DesignPagingScaffold<UiBlock>(
            state = derivedState,
            modifier = Modifier.fillMaxSize(),
            onRefresh = { viewModel.blockList(userId, Pageable(0, postLimit)) },
            placeholder = {
                SearchItemSkeleton(modifier = Modifier.padding(horizontal = 16.dp))
            },
            errorContent = { error, refresh ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) {
                    Spacer(modifier = Modifier.height(8.dp))
                    DesignErrorLabel(
                        refresh,
                        error,
                        component.resource(),
                        PaddingValues(horizontal = 16.dp)
                    )
                }
            }
        ) { state, lazyPagingItems ->
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(
                    count = lazyPagingItems.itemCount,
                    key = { lazyPagingItems[it]?.userId ?: it }
                ) { index ->
                    lazyPagingItems[index]?.let { block ->
                        val member = UiMember(
                            id = block.userId,
                            username = block.username,
                            slug = block.slug.toString(),
                            imageUrl = block.image
                        )
                        Peer(
                            member = member,
                            onClick = onClick
                        )
                    }
                }
            }
        }
    }
    DesignTitleBarHost("BlockScreen") {
        titleBar {
            DesignTitle {
                Text(stringResource(R.string.blocklist_label))
            }
        }
    }
}
