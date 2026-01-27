package eu.peernetwork.wallet.ui.transactions

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.theme.DesignTheme

@Composable
fun TransactionsSkeleton(modifier: Modifier = Modifier) {
    Row(
        modifier = Modifier
            .then(modifier)
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surfaceDim)
            .padding(10.dp)
    ) {
        Box(modifier = Modifier.size(36.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.background))
    }
}

@Composable
@Preview
fun PreviewTransactionsSkeleton() {
    DesignTheme(isDarkMode = true) {
        TransactionsSkeleton(
            modifier = Modifier.padding(16.dp)
        )
    }
}
