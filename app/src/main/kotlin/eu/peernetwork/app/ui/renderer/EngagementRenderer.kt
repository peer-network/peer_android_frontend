package eu.peernetwork.app.ui.renderer

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import eu.peernetwork.app.mapper.toUiToken
import eu.peernetwork.blog.ui.engagement.EngagementConfirmation
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.wallet.ui.confirmation.ConfirmationScreen
import javax.inject.Inject

class EngagementRenderer @Inject constructor(
    private val provider: UiComponentProvider
) : EngagementConfirmation {
    @Composable
    override fun invoke(
        modifier: Modifier,
        spec: EngagementConfirmation.Spec
    ) {
        val show = remember(spec.type.value) { mutableStateOf(spec.type.value != null) }
        ConfirmationScreen(
            spec.type.value?.toUiToken(),
            show,
            provider,
            spec.viewModelStoreOwner,
            { spec.type.value = null }
        ) {
            if (it) {
                spec.type.value?.let { spec.onConfirm(it) }
            }
            show.value = false
        }
    }
}
