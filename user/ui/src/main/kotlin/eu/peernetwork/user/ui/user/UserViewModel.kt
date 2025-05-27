package eu.peernetwork.user.ui.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eu.peernetwork.user.domain.usecase.AuthUserUsecase
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
    private val authUserUsecase: AuthUserUsecase,
    private val observerUsecase: ObserveAuthUserUsecase
) : ViewModel() {
    private val mutableState = MutableStateFlow<State>(State.Empty)

    val state: StateFlow<State> = mutableState.asStateFlow()

    fun initialize() {
        viewModelScope.launch {
            observerUsecase().collectLatest {
                (mutableState.value as? State.Success?)?.let { state ->
                    val isConfigurable = it?.id == state.account.id
                    if (isConfigurable) {
                        mutableState.tryEmit(State.Success(it!!, true))
                    }
                }
            }
        }
    }

    fun getAccount(id: String) {
        mutableState.tryEmit(State.Loading)
        viewModelScope.launch {
            try {
                val account = userUsecase(usecase(id))
                val principal = observerUsecase().firstOrNull()?.id ?: authUserUsecase().mapFromDomain().id
                mutableState.tryEmit(State.Success(account, principal == account.id))
            } catch (error: Throwable) {
                mutableState.tryEmit(State.Error(error))
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
