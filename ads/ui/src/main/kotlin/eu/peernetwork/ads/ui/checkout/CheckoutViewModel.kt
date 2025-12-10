package eu.peernetwork.ads.ui.checkout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eu.peernetwork.ads.domain.usecase.BoostUsecase
import eu.peernetwork.ads.ui.mapper.mapFromDomain
import eu.peernetwork.ads.ui.model.UiOrder
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class CheckoutViewModel @Inject constructor(
    private val usecase: BoostUsecase
) : ViewModel() {
    private val _state = MutableStateFlow<State>(State.Default)

    val state: StateFlow<State> = _state.asStateFlow()

    operator fun invoke(id: String) {
        viewModelScope.launch {
            try {
                _state.tryEmit(State.Loading)
                _state.tryEmit(State.Success(usecase(id).mapFromDomain()))
            } catch (error: Throwable) {
                _state.tryEmit(State.Error(error))
            }
        }
    }

    fun reset() {
        viewModelScope.launch {
            _state.tryEmit(State.Default)
        }
    }

    sealed interface State {
        data object Default: State
        data object Loading: State
        data class Success(val order: UiOrder): State
        data class Error(val error: Throwable): State
    }
}
