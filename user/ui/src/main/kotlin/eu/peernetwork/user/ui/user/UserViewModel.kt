package eu.peernetwork.user.ui.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eu.peernetwork.user.domain.usecase.AuthRefreshUsecase
import eu.peernetwork.user.domain.usecase.ProfileUsecase
import eu.peernetwork.user.ui.mapper.mapFromDomain
import eu.peernetwork.user.ui.model.UiAccount
import eu.peernetwork.user.ui.usecase.ObserveAuthUserUsecase
import eu.peernetwork.user.ui.usecase.UserUsecase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@User.Scope
class UserViewModel @Inject constructor(
    private val usecase: ProfileUsecase,
    private val userUsecase: UserUsecase,
    private val authUserUsecase: AuthRefreshUsecase,
    private val observerUsecase: ObserveAuthUserUsecase
) : ViewModel() {
    private val _state = MutableStateFlow<State>(State.Empty)

    val state: StateFlow<State> = _state.asStateFlow()

    fun initialize() {
        viewModelScope.launch {
            observerUsecase().collectLatest {
                (_state.value as? State.Success?)?.let { state ->
                    val isConfigurable = it?.id == state.account.id
                    if (isConfigurable) {
                        _state.tryEmit(State.Success(state.account, true))
                    }
                }
            }
        }
    }

    fun getAccount(id: String) {
        _state.tryEmit(State.Loading)
        viewModelScope.launch {
            try {
                val user = observerUsecase().firstOrNull()
                val principal = if (user?.id != id) {
                    authUserUsecase().mapFromDomain().id
                } else {
                    user.id
                }
                val account = userUsecase(usecase(id))
                _state.tryEmit(State.Success(account, principal == account.id))
            } catch (error: Throwable) {
                _state.tryEmit(State.Error(error))
            }
        }
    }

    sealed interface State {
        data object Empty : State
        data object Loading : State
        data class Success(
            val account: UiAccount,
            val configurable: Boolean
        ) : State
        data class Error(val error: Throwable) : State
    }
}
