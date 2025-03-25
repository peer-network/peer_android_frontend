package eu.peernetwork.user.ui.user.coupon

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eu.peernetwork.user.domain.usecase.CouponUsecase
import eu.peernetwork.user.ui.mapper.mapFromDomain
import eu.peernetwork.user.ui.model.UiCoupon
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

class UserCouponViewModel @Inject constructor(
    private val usecase: CouponUsecase
) : ViewModel() {
    private val mutableState = MutableStateFlow<State>(State.Loading)

    val state: StateFlow<State> = mutableState.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = State.Initialize
    )

    fun getCoupons() {
        mutableState.tryEmit(State.Loading)
        viewModelScope.launch {
            try {
                mutableState.tryEmit(State.Success(usecase().map { it.mapFromDomain() }))
            } catch (error: Throwable) {
                mutableState.tryEmit(State.Error(error))
            }
        }
    }

    sealed interface State {
        data object Initialize : State
        data object Loading : State
        data class Success(val coupons: List<UiCoupon>) : State
        data class Error(val error: Throwable) : State
    }
}
