package eu.peernetwork.wallet.ui.confirmation

import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.wallet.ui.mapper.v2.mapToDisclaimer
import eu.peernetwork.wallet.ui.mapper.v2.mapToFreeTitle
import eu.peernetwork.wallet.ui.mapper.v2.mapToIcon
import eu.peernetwork.wallet.ui.mapper.v2.mapToTitle
import eu.peernetwork.wallet.ui.model.v2.UiToken
import java.math.BigDecimal
import java.math.RoundingMode

@Composable
fun ConfirmationModal(
    token: UiToken,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    val updatedOnDismiss by rememberUpdatedState(onDismiss)
    val updatedOnConfirm by rememberUpdatedState(onConfirm)
    ConfirmationScreen(
        provider = provider,
        viewModelStoreOwner = viewModelStoreOwner
    ) { component, viewModel ->
        ConfirmationScreen(
            token = token,
            component = component,
            viewModel = viewModel,
            onCancel = { updatedOnDismiss() },
        ) { quote ->
            ConfirmationPage(
                painter = token.mapToIcon(),
                title = if (quote.value.available <= 0) {
                    token.mapToFreeTitle()
                } else {
                    token.mapToTitle()
                },
                label = token.mapToDisclaimer(),
                price = if (quote.value.available > 0) {
                    "${BigDecimal(0).setScale(2, RoundingMode.HALF_UP)}"
                } else {
                    "${quote.value.price.setScale(2, RoundingMode.HALF_UP)}"
                },
                modifier = Modifier
                    .padding(16.dp)
                    .navigationBarsPadding(),
                onCancel = { updatedOnDismiss() },
                onConfirm = { updatedOnConfirm() }
            )
        }
    }
}
