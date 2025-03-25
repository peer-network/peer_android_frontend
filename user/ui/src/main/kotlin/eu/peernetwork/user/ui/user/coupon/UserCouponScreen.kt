package eu.peernetwork.user.ui.user.coupon

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
import eu.peernetwork.user.ui.model.UiCoupon
import eu.peernetwork.user.ui.user.component.UserCoupons

@Composable
fun UserCouponScreen(
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
) {
    val context = LocalContext.current
    val component = remember {
        provider.builder(UserCoupon.Builder::class.java).build(context)
    }
    val viewModel = viewModel(
        modelClass = UserCouponViewModel::class.java,
        viewModelStoreOwner = viewModelStoreOwner,
        factory = component.viewModelFactory()
    )
    val state by viewModel.state.collectAsStateWithLifecycle()
    val coupons = remember { mutableStateOf<List<UiCoupon>?>(
        (state as? UserCouponViewModel.State.Success?)?.coupons
    ) }
    Crossfade(targetState = coupons.value) {
        when (it) {
            null -> { Box {} }
            else -> UserCoupons(it)
        }
    }
    LaunchedEffect(state) {
        when (state) {
            is UserCouponViewModel.State.Initialize -> viewModel.getCoupons()
            is UserCouponViewModel.State.Loading -> coupons.value = null
            is UserCouponViewModel.State.Success -> {
                coupons.value = (state as UserCouponViewModel.State.Success).coupons
            }
            is UserCouponViewModel.State.Error -> {}
        }
    }
}
