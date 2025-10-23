package eu.peernetwork.user.ui.v2.password.verification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eu.peernetwork.user.domain.usecase.VerifyTokenUsecase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class VerificationViewModel @Inject constructor(
    private val usecase: VerifyTokenUsecase
) : ViewModel() {
    private val _state = MutableStateFlow<State>(State.Empty)

    val state: StateFlow<State> = _state.asStateFlow()

    operator fun invoke(token: String) {
        viewModelScope.launch {
            try {
                _state.tryEmit(State.Loading)
                usecase(token)
                _state.tryEmit(State.Success(token))
            } catch (error: Throwable) {
                _state.tryEmit(State.Error(error))
            }
        }
    }

    fun reset() {
        viewModelScope.launch {
            _state.tryEmit(State.Empty)
        }
    }

    sealed interface State {
        data object Empty : State
        data object Loading : State

        data class Success(val token: String) : State
        data class Error(val error: Throwable) : State
    }
}
