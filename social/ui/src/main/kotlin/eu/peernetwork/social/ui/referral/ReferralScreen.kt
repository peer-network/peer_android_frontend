package eu.peernetwork.social.ui.referral

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import eu.peernetwork.core.common.paging.Pageable
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.luna.DesignPagingStream
import eu.peernetwork.core.ui.design.luna.DesignStreamState
import eu.peernetwork.core.ui.design.material.DesignTitle
import eu.peernetwork.core.ui.design.material.DesignTitleBarHost
import eu.peernetwork.core.ui.extension.builder
import eu.peernetwork.core.ui.extension.error
import eu.peernetwork.social.ui.R
import eu.peernetwork.social.ui.connection.ConnectionButton
import eu.peernetwork.social.ui.connection.ConnectionInteractor.Companion.LocalConnectionInteractor
import eu.peernetwork.social.ui.connection.ConnectionScreen

@Composable
fun ReferralScreen(
    userId: String,
    postLimit: Int,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    onClick: (String) -> Unit
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
                ReferralViewModel.State.Empty -> DesignStreamState.Default
                ReferralViewModel.State.Loading -> DesignStreamState.Loading
                is ReferralViewModel.State.Success -> {
                    DesignStreamState.Success(
                        (state as ReferralViewModel.State.Success).content
                    )
                }
                is ReferralViewModel.State.Error -> DesignStreamState.Error(
                    (state as ReferralViewModel.State.Error).error
                )
            }
        }
    }
    val handleClick by rememberUpdatedState(onClick)
    ReferralPage(
        onRefresh = { viewModel.referral(userId, Pageable(0, postLimit)) },
        header = { ReferralHeader(provider, viewModelStoreOwner) }
    ) {
        ConnectionScreen(
            provider = component,
            viewModelStoreOwner = viewModelStoreOwner
        ) {
            val controller = LocalConnectionInteractor.current
            val connection by controller.observe().collectAsState()
            DesignPagingStream(
                state = derivedState,
                modifier = Modifier.fillMaxSize(),
                loading = { ReferralSkeleton(3) },
                error = { error ->
                    ReferralError(component.resource().error(error.value)) {
                        viewModel.referral(userId, Pageable(0, postLimit))
                    }
                }
            ) { lazyPagingItems ->
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(
                        count = lazyPagingItems.itemCount,
                        key = { lazyPagingItems[it]?.id ?: it }
                    ) { index ->
                        lazyPagingItems[index]?.let { referral ->
                            ReferralItem(
                                slug = referral.slug,
                                username = referral.username,
                                imageUrl = referral.img,
                                onClick = { handleClick(referral.id) }
                            ) {
                                ConnectionButton(
                                    isFollowing = connection.getOrDefault(
                                        key = referral.id,
                                        defaultValue = referral.isFollowing
                                    ),
                                    isFollowed = referral.isFollowed,
                                    onClick = { follow ->
                                        controller.invoke(referral.id, !follow)
                                    },
                                    fontWeight = FontWeight.SemiBold,
                                    minHeight = 32.dp,
                                    contentPadding = PaddingValues(
                                        horizontal = 16.dp,
                                        vertical = 8.dp
                                    )
                                )
                            }
                        }
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
    LaunchedEffect(Unit) {
        if (state is ReferralViewModel.State.Empty) {
            viewModel.referral(userId, Pageable(0, postLimit))
        }
    }
}
