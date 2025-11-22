package eu.peernetwork.wallet.ui.balance

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eu.peernetwork.wallet.domain.usecase.OverviewUsecase
import eu.peernetwork.wallet.ui.mapper.mapFromDomain
import eu.peernetwork.wallet.ui.model.UiWallet
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class BalanceViewModel @Inject constructor(
    private val usecase: OverviewUsecase
) : ViewModel() {
    private val _state = MutableStateFlow<State>(State.Empty)

    val state: StateFlow<State> = _state.asStateFlow()

    operator fun invoke() {
        viewModelScope.launch {
            _state.tryEmit(State.Loading)
            try {
                _state.tryEmit(State.Success(usecase().mapFromDomain()))
            } catch (error: Throwable) {
                _state.tryEmit(State.Error(error))
            }
        }
    }

    sealed interface State {
        data object Empty : State
        data object Loading : State
        data class Success(val wallet: UiWallet) : State
        data class Error(val error: Throwable) : State
    }
}
