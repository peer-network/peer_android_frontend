package eu.peernetwork.user.ui.user.core

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eu.peernetwork.user.ui.model.UiAccount
import eu.peernetwork.user.ui.usecase.ObserveAuthUserUsecase
import eu.peernetwork.user.ui.usecase.ProfileUsecase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@User.Scope
class UserViewModel @Inject constructor(
    private val usecase: ProfileUsecase,
    observerUsecase: ObserveAuthUserUsecase
) : ViewModel() {
    private val mutableState = MutableStateFlow<State>(State.Initialize)

    val state: StateFlow<State> = mutableState
        .combine(observerUsecase()) { state, account ->
            account?.let { State.Success(it) } ?: state
        }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = State.Initialize
    )

    fun getAccount() {
        mutableState.tryEmit(State.Loading)
        viewModelScope.launch {
            try {
                usecase()
            } catch (error: Throwable) {
                mutableState.tryEmit(State.Error(error))
            }
        }
    }

    sealed interface State {
        data object Initialize : State
        data object Loading : State
        data class Success(val account: UiAccount) : State
        data class Error(val error: Throwable) : State
    }
}
