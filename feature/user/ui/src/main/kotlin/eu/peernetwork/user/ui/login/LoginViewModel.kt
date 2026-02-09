package eu.peernetwork.user.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eu.peernetwork.user.domain.usecase.LoginUsecase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    private val loginUsecase: LoginUsecase
) : ViewModel() {
    private val mutableState = MutableStateFlow<State>(State.Initial)

    val state: StateFlow<State> = mutableState.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Companion.WhileSubscribed(5_000),
        initialValue = State.Initial
    )

    fun login(email: String, password: String, rememberMe: Boolean) {
        mutableState.tryEmit(State.Loading)
        viewModelScope.launch {
            try {
                mutableState.tryEmit(
                    State.Success(
                        loginUsecase(LoginUsecase.Parameter(email, password, rememberMe))
                    )
                )
            } catch (error: Throwable) {
                mutableState.tryEmit(State.Error(error))
            }
        }
    }

    fun reset() {
        mutableState.tryEmit(State.Initial)
    }

    sealed interface State {
        data object Initial : State
        data object Loading : State
        data class Success(val toke: String) : State
        data class Error(val error: Throwable) : State
    }
}