package eu.peernetwork.wallet.ui.transfer

import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.luna.DesignStream
import eu.peernetwork.core.ui.design.luna.DesignStreamState
import eu.peernetwork.core.ui.extension.builder
import eu.peernetwork.wallet.ui.R
import eu.peernetwork.wallet.ui.balance.BalanceOverview
import eu.peernetwork.wallet.ui.extension.route
import eu.peernetwork.wallet.ui.rate.RateScreen
import eu.peernetwork.wallet.ui.transactions.TransactionsNavigator.Companion.LocalTransactionsNavigator
import eu.peernetwork.wallet.ui.transactions.TransactionsSummeryItem
import java.math.RoundingMode

@Composable
fun TransferCheckout(
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    onUserClicked: (String) -> Unit,
    onBack: () -> Unit,
    onFinish: () -> Unit
) {
    val context = LocalContext.current
    val navigator = LocalTransactionsNavigator.current
    val component = remember {
        provider.builder(Transfer.Builder::class.java).build(context)
    }
    val viewModel = viewModel(
        modelClass = TransferViewModel::class.java,
        viewModelStoreOwner = viewModelStoreOwner,
        factory = component.viewModelFactory()
    )
    val state by viewModel.state.collectAsStateWithLifecycle()
    val status by viewModel.status.collectAsStateWithLifecycle()
    val handleFinish by rememberUpdatedState(onFinish)
    val handleUserClick by rememberUpdatedState(onUserClicked)
    val streamState = remember { derivedStateOf {
        if (status is TransferViewModel.Status.Empty) {
            DesignStreamState.Default
        } else {
            DesignStreamState.Success(
                (status as? TransferViewModel.Status.Confirmation?)?.detail
                    ?: (status as? TransferViewModel.Status.Checkout?)?.detail!!
            )
        }
    } }
    val initialized = remember { mutableStateOf(false) }
    val lastUpdated = rememberSaveable { mutableLongStateOf(System.currentTimeMillis()) }
    val isLoading = remember { derivedStateOf { state is TransferViewModel.State.Loading } }
    val isSuccess = remember { derivedStateOf { state is TransferViewModel.State.Success } }
    val enabled = remember { derivedStateOf { !isSuccess.value } }
    val error = remember { derivedStateOf { (state as? TransferViewModel.State.Error?)?.error } }
    DesignStream(
        state = streamState,
        default = { LaunchedEffect(Unit) { handleFinish() } }
    ) {
        val price = remember { derivedStateOf {
            it.value.amount.setScale(6, RoundingMode.HALF_UP)
        } }
        RateScreen(
            provider = component,
            viewModelStoreOwner = viewModelStoreOwner
        ) { service, action ->
            DesignStream(state = service) { rate ->
                val peer = (rate.value.peer * 100).toInt()
                val burn = (rate.value.burn * 100).toInt()
                val invite = (rate.value.percentage * 100).toInt()
                val total = remember { derivedStateOf {
                    price.value +
                            (price.value * rate.value.percentage.toBigDecimal()) +
                            (price.value * rate.value.burn.toBigDecimal()) +
                            (price.value * rate.value.peer.toBigDecimal())
                } }
                TransferPreview(
                    price = price.value.toString(),
                    recipient = it.value.recipient,
                    message = it.value.message,
                    enabled = enabled,
                    isLoading = isLoading,
                    onRefresh = { lastUpdated.longValue = System.currentTimeMillis() },
                    onBack = onBack,
                    onAuthorClicked = { handleUserClick(it.value.recipient.id) },
                    onMessageClicked = { spec, value -> navigator.navigate(spec.route(value)) },
                    onSend = {
                        viewModel.transfer(
                            price = it.value.amount,
                            recipient = it.value.recipient.id,
                            message = it.value.message,
                        )
                    },
                    rate = {
                        TransactionsSummeryItem(
                            title = stringResource(R.string.platform_charge, "$peer"),
                            price = "${(price.value * rate.value.peer.toBigDecimal())
                                .setScale(8, RoundingMode.HALF_UP)
                                .toPlainString()}",
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )
                        TransactionsSummeryItem(
                            title = stringResource(R.string.burn_charge, "$burn"),
                            price = "${(price.value * rate.value.burn.toBigDecimal())
                                .setScale(8, RoundingMode.HALF_UP)
                                .toPlainString()}",
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )
                        if (rate.value.percentage > 0) {
                            TransactionsSummeryItem(
                                title = stringResource(R.string.invite_charge, "$invite"),
                                price = "${(price.value * rate.value.percentage.toBigDecimal())
                                    .setScale(8, RoundingMode.HALF_UP)}",
                                modifier = Modifier.padding(horizontal = 8.dp)
                            )
                        }
                        TransactionsSummeryItem(
                            title = stringResource(R.string.total_amount),
                            color = MaterialTheme.colorScheme.onBackground,
                            price = "${total.value.setScale(6, RoundingMode.HALF_UP)}",
                            modifier = Modifier.padding(horizontal = 8.dp)
                                .padding(top = 4.dp)
                        )
                    }
                ) {
                    BalanceOverview(
                        color = MaterialTheme.colorScheme.surfaceContainerLowest,
                        offset = total.value,
                        provider = component,
                        viewModelStoreOwner = viewModelStoreOwner
                    ) { component, model ->
                        LaunchedEffect(lastUpdated.longValue) {
                            if (initialized.value) {
                                model()
                            } else {
                                initialized.value = true
                            }
                        }
                        TransferSuccess(
                            showSheet = isSuccess,
                            modifier = Modifier.navigationBarsPadding()
                                .padding(16.dp)
                        ) {
                            model()
                            handleFinish()
                        }
                    }
                }
                TransferError(
                    error = error,
                    component = component,
                    modifier = Modifier.navigationBarsPadding()
                        .padding(16.dp),
                    onReset = { viewModel.cancel() }
                ) {
                    viewModel.transfer(
                        price = it.value.amount,
                        recipient = it.value.recipient.id,
                        message = it.value.message,
                    )
                }
            }
        }
    }
    LaunchedEffect(Unit) {
        (status as? TransferViewModel.Status.Confirmation?)?.let {
            viewModel.checkout(it.detail)
        }
    }
}
