package eu.peernetwork.social.ui.referral

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
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
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
import eu.peernetwork.social.ui.model.UiMember
import eu.peernetwork.social.ui.model.UiReferral

@Composable
fun ReferralScreen(
    userId: String,
    postLimit: Int,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    onClick: (UiMember) -> Unit
) {
    val context = LocalContext.current
    val component = remember {
        provider.builder(Referral.Builder::class.java).build(context)
    }
    val viewModel = viewModel(
        modelClass = ReferralViewModel::class.java,
        viewModelStoreOwner = viewModelStoreOwner,
        factory = component.viewModelFactory()
    )
    val state by viewModel.state.collectAsState()
    val derivedState = remember {
        derivedStateOf {
            when (state) {
                ReferralViewModel.State.Empty -> DesignStatefulScaffoldState.Empty
                ReferralViewModel.State.Loading -> DesignStatefulScaffoldState.Loading
                is ReferralViewModel.State.Success -> {
                    DesignStatefulScaffoldState.Success(
                        (state as ReferralViewModel.State.Success).content
                    )
                }
                is ReferralViewModel.State.Error -> DesignStatefulScaffoldState.Error(
                    (state as ReferralViewModel.State.Error).error
                )
            }
        }
    }
    DesignPagingScaffold<UiReferral>(
        state = derivedState,
        modifier = Modifier.fillMaxSize(),
        onRefresh = { viewModel.referral(userId, Pageable(0, postLimit)) },
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
        val refreshState = remember {
            derivedStateOf {
                when (val refresh = lazyPagingItems.loadState.refresh) {
                    is LoadState.Loading -> DesignStatefulScaffoldState.Loading
                    is LoadState.Error -> DesignStatefulScaffoldState.Error(refresh.error)
                    else -> state.value
                }
            }
        }
        DesignRefreshableScaffold<LazyPagingItems<UiReferral>>(
            state = refreshState,
            onRefresh = { lazyPagingItems.refresh() }
        ) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                item { ReferralHeader(userId, provider, viewModelStoreOwner) }
                items(
                    count = lazyPagingItems.itemCount,
                    key = { lazyPagingItems[it]?.id ?: it }
                ) { index ->
                    lazyPagingItems[index]?.let { referral ->
                        val member = UiMember(
                            id = referral.id,
                            username = referral.username,
                            slug = referral.slug,
                            imageUrl = referral.img
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
    DesignTitleBarHost("ReferralScreen") {
        titleBar {
            DesignTitle {
                Text(stringResource(R.string.referrals_label))
            }
        }
    }
}
