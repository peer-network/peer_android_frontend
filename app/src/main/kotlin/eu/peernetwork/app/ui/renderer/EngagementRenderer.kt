package eu.peernetwork.app.ui.renderer

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import eu.peernetwork.app.mapper.toUiToken
import eu.peernetwork.blog.ui.engagement.EngagementModal
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.luna.DesignStream
import eu.peernetwork.core.ui.design.luna.DesignStreamState
import eu.peernetwork.core.ui.design.material.DesignBottomSheetScaffold
import eu.peernetwork.wallet.ui.confirmation.ConfirmationModal
import javax.inject.Inject

class EngagementRenderer @Inject constructor(
    private val provider: UiComponentProvider
) : EngagementModal {
    @Composable
    override fun invoke(
        modifier: Modifier,
        spec: EngagementModal.Spec
    ) {
        val showSheet = remember { derivedStateOf { spec.type.value != null } }
        val streamState = remember { derivedStateOf {
            if (spec.type.value == null) {
                DesignStreamState.Default
            } else {
                DesignStreamState.Success(spec.type.value!!)
            }
        } }
        DesignStream(state = streamState) { intent ->
            DesignBottomSheetScaffold(
                state = showSheet,
                dismissable = true,
                color = MaterialTheme.colorScheme.surfaceDim,
                onDismiss = { spec.type.value = null }
            ) {
                ConfirmationModal(
                    token = intent.value.toUiToken(),
                    provider = provider,
                    viewModelStoreOwner = spec.viewModelStoreOwner,
                    onDismiss = {
                        spec.type.value = null
                        spec.onDismiss()
                    }
                ) {
                    spec.type.value?.let { spec.onConfirm(it) }
                    spec.type.value = null
                }
            }
        }
    }
}
