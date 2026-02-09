package eu.peernetwork.wallet.ui.transactions

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.feature.wallet.ui.R
import eu.peernetwork.wallet.ui.extension.route
import eu.peernetwork.wallet.ui.mapper.format
import eu.peernetwork.wallet.ui.transactions.TransactionsNavigator.Companion.LocalTransactionsNavigator

@Composable
fun TransactionsList(
    uuid: String,
    limit: Int,
    lastUpdated: State<Long>,
    listState: LazyListState,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    onTransaction: () -> Unit,
    onClick: (String) -> Unit
) {
    val navigator = LocalTransactionsNavigator.current
    val handleClick by rememberUpdatedState(onClick)
    TransactionsScreen(
        limit = limit,
        lastUpdated = lastUpdated,
        provider = provider,
        onClick = onTransaction,
        viewModelStoreOwner = viewModelStoreOwner
    ) { component, items, rate ->
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 36.dp),
        ) {
            items(
                count = items.itemCount,
                key = { index -> items[index]?.id?.let { "$it;$index" } ?: index }
            ) { index ->
                items[index]?.let { transaction ->
                    val isVisible = rememberSaveable { mutableStateOf(false) }
                    val expanded = rememberSaveable { mutableStateOf(false) }
                    val isRecipient = transaction.recipient.id == uuid
                    val profile = if (isRecipient) {
                        transaction.sender
                    } else {
                        transaction.recipient
                    }
                    val title = transactionsUsername(
                        isVisible = isVisible,
                        isRecipient = isRecipient,
                        user = profile
                    )
                    TransactionsItem(
                        title = transaction.res?.let {
                            buildAnnotatedString { append(stringResource(it)) }
                        } ?: title,
                        description = transaction.message,
                        createAt = transaction.createdAt,
                        price = if (isRecipient) {
                            transaction.amount.net.format()
                        } else {
                            transaction.amount.gross.format()
                        },
                        expanded = expanded,
                        leading = {
                            TransactionsAvatar(
                                icon = painterResource(R.drawable.ic_transfer_direction),
                                contentDescription = transaction.res?.let {
                                    stringResource(it)
                                } ?: title.text,
                                isRecipient = isRecipient
                            ) {
                                if (transaction.icon != null && transaction.res != null) {
                                    Icon(
                                        painter = painterResource(transaction.icon),
                                        contentDescription = stringResource(transaction.res),
                                        tint = MaterialTheme.colorScheme.onBackground,
                                        modifier = Modifier
                                            .background(MaterialTheme.colorScheme.background)
                                            .padding(8.dp)
                                    )
                                } else {
                                    TransactionsProfile(
                                        user = profile,
                                        isVisible = isVisible,
                                    ) { handleClick(profile.id) }
                                }
                            }
                        },
                        onProfileClicked = {
                            if (transaction.res == null) {
                                handleClick(profile.id)
                            } else {
                                expanded.value = !expanded.value
                            }
                        },
                        onMessageClicked = { spec, value -> navigator.navigate(spec.route(value)) },
                        modifier = Modifier.padding(bottom = 10.dp)
                    ) {
                        TransactionsSummeryItem(
                            title = stringResource(R.string.transaction_amount_label),
                            price = if (isRecipient) {
                                transaction.amount.gross.format()
                            } else {
                                transaction.amount.net.format()
                            },
                        )
                        transaction.fees?.let {
                            val peer = (rate.peer * 100).toInt()
                            val burn = (rate.burn * 100).toInt()
                            val invite = (rate.percentage * 100).toInt()
                            TransactionsSummeryItem(
                                title = stringResource(R.string.platform_charge, "$peer"),
                                price = it.peer.format(),
                            )
                            TransactionsSummeryItem(
                                title = stringResource(R.string.burn_charge, "$burn"),
                                price = it.burn.format(),
                            )
                            if (rate.percentage > 0) {
                                TransactionsSummeryItem(
                                    title = stringResource(R.string.invite_charge, "$invite"),
                                    price = it.commission.format(),
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
