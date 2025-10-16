package eu.peernetwork.user.ui.v2.referral

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eu.peernetwork.user.domain.usecase.ReferralUsecase
import eu.peernetwork.user.domain.usecase.ReferralVerificationUsecase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class ReferralViewModel @Inject constructor(
    private val usecase: ReferralUsecase,
    private val verificationUsecase: ReferralVerificationUsecase
) : ViewModel() {
    private val _status = MutableStateFlow<Status>(Status.Initial)

    private val _state = MutableStateFlow<State>(State.Initial)

    val status: StateFlow<Status> = _status.asStateFlow()

    val state: StateFlow<State> = _state.asStateFlow()

    fun getDefaultReferral() {
        viewModelScope.launch {
            _status.tryEmit(Status.Loading)
            try {
                _status.tryEmit(Status.Success(usecase()))
            } catch (error: Throwable) {
                _status.tryEmit(Status.Error(error))
            }
        }
    }

    fun verify(code: String) {
        viewModelScope.launch {
            _state.tryEmit(State.Loading)
            try {
                _state.tryEmit(State.Success(verificationUsecase(code).id))
            } catch (error: Throwable) {
                _state.tryEmit(State.Error(error))
            }
        }
    }

    fun reset() {
        _status.tryEmit(Status.Initial)
        _state.tryEmit(State.Initial)
    }

    sealed interface Status {
        data object Initial : Status
        data object Loading : Status
        data class Success(val referral: String) : Status
        data class Error(val error: Throwable) : Status
    }

    sealed interface State {
        data object Initial : State
        data object Loading : State
        data class Success(val referral: String) : State
        data class Error(val error: Throwable) : State
    }
}
