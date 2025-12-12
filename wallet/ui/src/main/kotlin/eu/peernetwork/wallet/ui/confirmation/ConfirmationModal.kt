package eu.peernetwork.wallet.ui.confirmation

import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.luna.DesignStream
import eu.peernetwork.core.ui.design.luna.DesignStreamState
import eu.peernetwork.core.ui.design.material.DesignBottomSheetScaffold
import eu.peernetwork.wallet.ui.mapper.v2.mapToDisclaimer
import eu.peernetwork.wallet.ui.mapper.v2.mapToFreeTitle
import eu.peernetwork.wallet.ui.mapper.v2.mapToIcon
import eu.peernetwork.wallet.ui.mapper.v2.mapToTitle
import eu.peernetwork.wallet.ui.model.v2.UiToken
import java.math.RoundingMode

@Composable
fun ConfirmationModal(
    token: MutableState<UiToken?>,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    val showSheet = remember(token.value) { mutableStateOf(token.value != null) }
    val updatedOnDismiss by rememberUpdatedState(onDismiss)
    val updatedOnConfirm by rememberUpdatedState(onConfirm)
    ConfirmationScreen(
        provider = provider,
        viewModelStoreOwner = viewModelStoreOwner
    ) { component, viewModel ->
        DesignBottomSheetScaffold(
            state = showSheet,
            color = MaterialTheme.colorScheme.surfaceDim,
            onDismiss = {
                token.value = null
                updatedOnDismiss()
            }
        ) {
            val streamState = remember { derivedStateOf {
                if (token.value == null) {
                    DesignStreamState.Default
                } else {
                    DesignStreamState.Success(token.value!!)
                }
            } }
            DesignStream(state = streamState) { token ->
                ConfirmationScreen(
                    token = token.value,
                    viewModel = viewModel
                ) { quote ->
                    ConfirmationPage(
                        painter = token.value.mapToIcon(),
                        title = if (quote.value.available <= 0) {
                            token.value.mapToFreeTitle()
                        } else {
                            token.value.mapToTitle()
                        },
                        label = token.value.mapToDisclaimer(),
                        price = "${quote.value.value.setScale(2, RoundingMode.HALF_UP)}",
                        modifier = Modifier
                            .padding(16.dp)
                            .navigationBarsPadding(),
                        onCancel = { updatedOnDismiss() },
                        onConfirm = { updatedOnConfirm() }
                    )
                }
            }
        }
    }
}
