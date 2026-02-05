package eu.peernetwork.wallet.ui.transactions

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.R
import eu.peernetwork.core.ui.theme.DesignTheme

@Composable
fun TransactionsError(
    error: State<Throwable?>,
    component: Transactions.Component,
    onRefresh: () -> Unit
) {
    error.value?.message?.let {
        TransactionsError(
            error = component.resource().string(it),
            onRefresh = onRefresh
        )
    }
}

@Composable
fun TransactionsError(
    error: String,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(24.dp),
    onRefresh: () -> Unit
) {
    val handleRefresh by rememberUpdatedState(onRefresh)
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .then(modifier)
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surfaceDim)
            .padding(contentPadding)
    ) {
        Text(
            text = error,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.weight(1f)
        )
        IconButton(onClick = onRefresh) {
            Icon(
                painter = painterResource(R.drawable.ic_refresh),
                contentDescription = stringResource(R.string.retry_label),
                tint = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(start = 16.dp)
                    .size(24.dp)
                    .clickable { handleRefresh() },
            )
        }
    }
}

@Composable
@Preview
fun PreviewTransactionsError() {
    DesignTheme(isDarkMode = true) {
        TransactionsError(
            error = "An unexpected error occurred while processing your payment. Please try again.",
            modifier = Modifier.padding(16.dp)
        ) {}
    }
}
