package eu.peernetwork.user.ui.user.point

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.extension.builder
import eu.peernetwork.user.ui.model.UiPoint
import eu.peernetwork.user.ui.user.component.UserPoints

@Composable
fun UserCouponScreen(
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
) {
    val context = LocalContext.current
    val component = remember {
        provider.builder(UserPoint.Builder::class.java).build(context)
    }
    val viewModel = viewModel(
        modelClass = UserPointViewModel::class.java,
        viewModelStoreOwner = viewModelStoreOwner,
        factory = component.viewModelFactory()
    )
    val state by viewModel.state.collectAsStateWithLifecycle()
    val coupons = remember { mutableStateOf<List<UiPoint>?>(
        (state as? UserPointViewModel.State.Success?)?.coupons
    ) }
    Crossfade(targetState = coupons.value) {
        when (it) {
            null -> { Box {} }
            else -> UserPoints(it)
        }
    }
    LaunchedEffect(state) {
        when (state) {
            is UserPointViewModel.State.Initialize -> viewModel.getPoints()
            is UserPointViewModel.State.Loading -> coupons.value = null
            is UserPointViewModel.State.Success -> {
                coupons.value = (state as UserPointViewModel.State.Success).coupons
            }
            is UserPointViewModel.State.Error -> {}
        }
    }
}
