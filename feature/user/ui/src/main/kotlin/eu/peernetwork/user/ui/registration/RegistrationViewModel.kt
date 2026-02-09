package eu.peernetwork.user.ui.registration

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eu.peernetwork.user.domain.usecase.ActivationUsecase
import eu.peernetwork.user.domain.usecase.RegistrationUsecase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

class RegistrationViewModel @Inject constructor(
    private val registrationUsecase: RegistrationUsecase,
    private val activationUsecase: ActivationUsecase,
) : ViewModel() {
    private val mutableState = MutableStateFlow<State>(State.Initial)

    val state: StateFlow<State> = mutableState.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Companion.WhileSubscribed(5_000),
        initialValue = State.Initial
    )

    fun register(username: String, email: String, password: String, referral: String?) {
        mutableState.tryEmit(State.Loading)
        viewModelScope.launch {
            try {
                val code = registrationUsecase(
                    RegistrationUsecase.Parameter(
                        email = email,
                        username = username,
                        password = password,
                        referral = referral
                    )
                )
                activationUsecase(code)
                mutableState.tryEmit(State.Success(code, email))
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
        data class Success(
            val uuid: String,
            val email: String
        ) : State
        data class Error(val error: Throwable) : State
    }
}