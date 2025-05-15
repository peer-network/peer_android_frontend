package eu.peernetwork.user.ui.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eu.peernetwork.user.domain.usecase.ProfileUsecase
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
    private val observerUsecase: ObserveAuthUserUsecase
) : ViewModel() {
    private val mutableState = MutableStateFlow<State>(State.Empty)

    val state: StateFlow<State> = mutableState.asStateFlow()

    fun getAccount(id: String) {
        mutableState.tryEmit(State.Loading)
        viewModelScope.launch {
            try {
                val account = observerUsecase().firstOrNull()
                if (account?.id == id) {
                    observerUsecase().collectLatest {
                        if (it != null) {
                            mutableState.tryEmit(State.Success(it, true))
                        }
                    }
                } else {
                    mutableState.tryEmit(State.Success(userUsecase(usecase(id)), false))
                }
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
            val isOwner: Boolean
        ) : State
        data class Error(val error: Throwable) : State
    }
}
