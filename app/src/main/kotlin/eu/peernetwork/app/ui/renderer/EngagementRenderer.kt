package eu.peernetwork.app.ui.renderer

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import eu.peernetwork.app.mapper.toUiToken
import eu.peernetwork.blog.ui.engagement.EngagementDialog
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.wallet.ui.confirmation.ConfirmationModal
import javax.inject.Inject

class EngagementRenderer @Inject constructor(
    private val provider: UiComponentProvider
) : EngagementDialog {
    @Composable
    override fun invoke(
        modifier: Modifier,
        spec: EngagementDialog.Spec
    ) {
        val token = remember(spec.type.value) { mutableStateOf(spec.type.value?.toUiToken()) }
        ConfirmationModal(
            token = token,
            provider = provider,
            viewModelStoreOwner = spec.viewModelStoreOwner,
            onDismiss = { spec.type.value = null }
        ) { spec.type.value?.let { spec.onConfirm(it) } }
    }
}
