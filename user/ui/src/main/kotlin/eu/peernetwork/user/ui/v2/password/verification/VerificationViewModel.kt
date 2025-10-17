package eu.peernetwork.user.ui.v2.password.verification

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class VerificationViewModel @Inject constructor() : ViewModel() {
    private val _state = MutableStateFlow<State>(State.Empty)

    val state: StateFlow<State> = _state.asStateFlow()

    operator fun invoke(token: String) {
    }

    sealed interface State {
        data object Empty : State
        data object Loading : State

        data class Success(val token: String) : State
        data class Error(val error: Throwable) : State
    }
}
