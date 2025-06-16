package eu.peernetwork.wallet.ui.service

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eu.peernetwork.wallet.domain.usecase.TaxUsecase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class ServiceViewModel @Inject constructor(
    private val usecase: TaxUsecase
) : ViewModel() {
    private val mutableState = MutableStateFlow<State>(State.Empty)

    val state: StateFlow<State> = mutableState

    fun initialize() {
        viewModelScope.launch {
            try {
                mutableState.tryEmit(State.Loading)
                mutableState.tryEmit(State.Success(usecase().percentage))
            } catch (error: Throwable) {
                mutableState.tryEmit(State.Error(error))
            }
        }
    }

    sealed interface State {
        data object Empty : State
        data object Loading : State
        data class Success(val tax: Double) : State
        data class Error(val error: Throwable) : State
    }
}
