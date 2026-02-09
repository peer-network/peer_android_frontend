package eu.peernetwork.user.ui.logout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eu.peernetwork.user.domain.usecase.LogoutUsecase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class LogoutViewModel @Inject constructor(
    private val logoutUsecase: LogoutUsecase,
) : ViewModel() {
    private val _state = MutableStateFlow<State>(State.Default)

    val state: StateFlow<State> = _state.asStateFlow()

    operator fun invoke() {
        _state.tryEmit(State.Loading)
        viewModelScope.launch {
            try {
                logoutUsecase()
                _state.tryEmit(State.Success)
            } catch (error: Throwable) {
                _state.tryEmit(State.Error(error))
            }
        }
    }

    sealed interface State {
        data object Default: State
        data object Loading: State
        data object Success: State
        data class Error(val error: Throwable): State
    }
}
