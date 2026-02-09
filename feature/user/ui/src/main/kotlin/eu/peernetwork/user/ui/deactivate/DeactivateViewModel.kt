package eu.peernetwork.user.ui.deactivate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eu.peernetwork.user.domain.usecase.DeactivationUsecase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class DeactivateViewModel @Inject constructor(
    private val deactivationUsecase: DeactivationUsecase
) : ViewModel() {
    private val _state = MutableStateFlow<State>(State.Default)

    val state: StateFlow<State> = _state.asStateFlow()

    operator fun invoke(password: String) {
        _state.tryEmit(State.Loading)
        viewModelScope.launch {
            try {
                deactivationUsecase(password)
                _state.tryEmit(State.Success)
            } catch (error: Throwable) {
                _state.tryEmit(State.Error(error))
            }
        }
    }

    fun reset() {
        viewModelScope.launch { _state.tryEmit(State.Default) }
    }

    sealed interface State {
        data object Default: State
        data object Loading: State
        data object Success: State
        data class Error(val error: Throwable): State
    }
}
