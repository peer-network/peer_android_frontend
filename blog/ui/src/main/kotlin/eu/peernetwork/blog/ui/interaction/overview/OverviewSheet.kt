package eu.peernetwork.blog.ui.interaction.overview

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import eu.peernetwork.blog.ui.model.UiEngagement
import eu.peernetwork.blog.ui.post.PostNavigator
import eu.peernetwork.blog.ui.post.PostNavigator.Companion.LocalPostNavigator
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.luna.DesignStream
import eu.peernetwork.core.ui.design.luna.DesignStreamState
import eu.peernetwork.core.ui.design.material.DesignCollapsibleBottomSheet

sealed interface OverviewSheetState {
    data object Hidden : OverviewSheetState
    data object Visible: OverviewSheetState

    data class Dismissing(val action: () -> Unit): OverviewSheetState
}

@Composable
fun OverviewSheet(
    uuid: String,
    state: MutableState<UiEngagement?>,
    limit: Int,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
) {
    val showSheet = remember(state.value) { mutableStateOf(state.value != null) }
    val sheetState = remember { mutableStateOf<OverviewSheetState>(OverviewSheetState.Hidden) }
    val streamState = remember { derivedStateOf {
        if (state.value == null) {
            DesignStreamState.Default
        } else {
            DesignStreamState.Success(state.value!!)
        }
    } }
    val navigator = LocalPostNavigator.current
    DesignStream(streamState) { engagement ->
        DesignCollapsibleBottomSheet(
            state = showSheet,
            peekHeight = 400.dp,
            onDismiss = {
                if (sheetState.value is OverviewSheetState.Dismissing) {
                    (sheetState.value as OverviewSheetState.Dismissing).action.invoke()
                }
                sheetState.value = OverviewSheetState.Hidden
            },
        ) {
            OverviewScreen(
                uuid = uuid,
                state = engagement.value,
                postLimit = limit,
                provider = provider,
                viewModelStoreOwner = viewModelStoreOwner
            ) {
                sheetState.value = OverviewSheetState.Dismissing {
                    navigator.navigate(PostNavigator.Route.Profile(it))
                }
                state.value = null
            }
        }
    }
    LaunchedEffect(state.value) {
        state.value?.let {
            sheetState.value = OverviewSheetState.Visible
        }
    }
}
