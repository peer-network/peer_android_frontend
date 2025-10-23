package eu.peernetwork.user.ui.v2.password.reset

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eu.peernetwork.user.domain.usecase.PasswordResetUsecase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class ResetViewModel @Inject constructor(
    private val usecase: PasswordResetUsecase
) : ViewModel() {
    private val mutableState = MutableStateFlow<State>(State.Empty)

    val state: StateFlow<State> = mutableState.asStateFlow()

    fun reset(token: String, password: String) {
        viewModelScope.launch {
            try {
                mutableState.tryEmit(State.Loading)
                usecase(PasswordResetUsecase.Parameter(token, password))
                mutableState.tryEmit(State.Success)
            } catch (error: Throwable) {
                mutableState.tryEmit(State.Error(error))
            }
        }
    }

    sealed interface State {
        data object Empty : State
        data object Loading : State
        data object Success : State
        data class Error(val error: Throwable) : State
    }
}