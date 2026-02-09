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

    private val _status = MutableStateFlow<Status>(Status.Empty)

    val state: StateFlow<State> = _state.asStateFlow()

    val status: StateFlow<Status> = _status.asStateFlow()

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

    fun updatedAt(timestamp: Long) {
        viewModelScope.launch {
            _status.tryEmit(Status.Success(timestamp))
        }
    }

    sealed interface Status {
        data object Empty : Status
        data object Loading : Status
        data class Success<T>(val data: T) : Status
        data class Error(val error: Throwable) : Status
    }

    sealed interface State {
        data object Empty : State
        data object Loading : State
        data class Success(val wallet: UiWallet) : State
        data class Error(val error: Throwable) : State
    }
}
