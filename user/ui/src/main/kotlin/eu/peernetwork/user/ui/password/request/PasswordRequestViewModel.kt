package eu.peernetwork.user.ui.password.request

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eu.peernetwork.user.domain.usecase.PasswordRequestUsecase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class PasswordRequestViewModel @Inject constructor(
    private val usecase: PasswordRequestUsecase
) : ViewModel() {
    private val mutableState = MutableStateFlow<State>(State.Empty)

    val state: StateFlow<State> = mutableState.asStateFlow()

    fun requestPassword(email: String) {
        viewModelScope.launch {
            try {
                mutableState.tryEmit(State.Loading)
                usecase(email)
                mutableState.tryEmit(State.Success(email))
            } catch (error: Throwable) {
                mutableState.tryEmit(State.Error(error))
            }
        }
    }

    sealed interface State {
        data object Empty : State
        data object Loading : State
        data class Success(val email: String) : State
        data class Error(val error: Throwable) : State
    }
}
