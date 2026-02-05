package eu.peernetwork.wallet.ui.balance

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.luna.DesignStream
import eu.peernetwork.core.ui.design.luna.DesignStreamState
import eu.peernetwork.wallet.ui.mapper.format
import java.math.BigDecimal
import java.math.RoundingMode

@Composable
@Suppress("UNCHECKED_CAST")
fun BalanceOverview(
    color: Color? = null,
    offset: BigDecimal = BigDecimal.ZERO,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    content: @Composable (Balance.Component, BalanceViewModel) -> Unit
) {
    val updatedContent by rememberUpdatedState(content)
    BalanceScreen(
        provider = provider,
        viewModelStoreOwner = viewModelStoreOwner,
    ) { component, viewModel ->
        val state by viewModel.state.collectAsStateWithLifecycle()
        val derivedState = remember {
            derivedStateOf {
                when (state) {
                    BalanceViewModel.State.Empty -> DesignStreamState.Default
                    BalanceViewModel.State.Loading -> DesignStreamState.Loading
                    is BalanceViewModel.State.Success -> {
                        DesignStreamState.Success(
                            (state as BalanceViewModel.State.Success).wallet
                        )
                    }
                    is BalanceViewModel.State.Error -> DesignStreamState.Error(
                        (state as BalanceViewModel.State.Error).error
                    )
                }
            }
        }
        DesignStream(
            state = derivedState,
            loading = { BalanceSkeleton() },
            error = { BalanceError(it, component) { viewModel() } }
        ) {
            val balance = remember { derivedStateOf {
                (it.value.balance - offset).setScale(8, RoundingMode.HALF_UP)
            } }
            BalancePreview(
                color = color,
                balance = balance.value.format()
            )
        }
        updatedContent(component, viewModel)
    }
}

@Composable
@Suppress("UNCHECKED_CAST")
fun BalanceOverview(
    lastUpdated: State<Long>,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
) {
    BalanceOverview(
        provider = provider,
        viewModelStoreOwner = viewModelStoreOwner,
    ) { component, viewModel ->
        val status by viewModel.status.collectAsStateWithLifecycle()
        LaunchedEffect(lastUpdated.value) {
            (status as? BalanceViewModel.Status.Success<Long>?)?.let {
                if (it.data != lastUpdated.value) {
                    viewModel()
                }
            } ?: viewModel()
            viewModel.updatedAt(lastUpdated.value)
        }
    }
}
