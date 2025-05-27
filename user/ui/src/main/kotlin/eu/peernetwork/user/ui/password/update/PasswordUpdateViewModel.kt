package eu.peernetwork.user.ui.password.update

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eu.peernetwork.user.domain.usecase.PasswordChangeUsecase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class PasswordUpdateViewModel @Inject constructor(
    private val usecase: PasswordChangeUsecase
) : ViewModel() {
    private val mutableState = MutableStateFlow<State>(State.Empty)

    val state: StateFlow<State> = mutableState.asStateFlow()

    fun update(current: String, new: String) {
        viewModelScope.launch {
            try {
                mutableState.tryEmit(State.Loading)
                usecase(PasswordChangeUsecase.Parameter(current, new))
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
