package eu.peernetwork.user.ui.option

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eu.peernetwork.user.domain.usecase.InvitationUsecase
import eu.peernetwork.user.ui.mapper.mapFromDomain
import eu.peernetwork.user.ui.model.UiInvite
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@Option.Scope
class OptionViewModel @Inject constructor(
    private val referralUsecase: InvitationUsecase
) : ViewModel() {
    private val _state = MutableStateFlow<State>(State.Empty)

    val state: StateFlow<State> = _state.asStateFlow()

    fun invite() {
        viewModelScope.launch {
            _state.tryEmit(State.Loading)
            try {
                _state.tryEmit(State.Success(referralUsecase().mapFromDomain()))
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
        data class Success(val invite: UiInvite) : State
        data class Error(val error: Throwable) : State
    }
}
