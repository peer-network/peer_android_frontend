package eu.peernetwork.app.ui.renderer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import eu.peernetwork.ads.ui.checkout.CheckoutBalance
import eu.peernetwork.ads.ui.overview.OverviewLabel
import eu.peernetwork.core.ui.annotation.UiViewModel
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.luna.DesignSkeleton
import eu.peernetwork.wallet.ui.R
import eu.peernetwork.wallet.ui.balance.BalanceScreen
import javax.inject.Inject

class CheckoutBalanceRenderer @Inject constructor(
    private val provider: UiComponentProvider
) : CheckoutBalance {
    @Composable
    override fun invoke(modifier: Modifier) {
        val viewModelStoreOwner = remember { UiViewModel.Owner() }
        BalanceScreen(
            provider = provider,
            viewModelStoreOwner = viewModelStoreOwner,
            loading = {
                DesignSkeleton(modifier = Modifier.padding(top = 10.dp)
                    .fillMaxWidth()
                    .height(48.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceDim))
            }
        ) {
            OverviewLabel(
                label = stringResource(R.string.balance_title),
                modifier = Modifier.fillMaxWidth()
                    .padding(top = 10.dp)
            ) { Text(it.value.balance.toString()) }
        }
    }
}
