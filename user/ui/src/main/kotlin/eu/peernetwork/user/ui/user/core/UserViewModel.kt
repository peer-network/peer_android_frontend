package eu.peernetwork.user.ui.user.core

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eu.peernetwork.core.common.provider.DispatcherProvider
import eu.peernetwork.user.domain.usecase.AuthenticatedUserUsecase
import eu.peernetwork.user.domain.usecase.DescriptionUsecase
import eu.peernetwork.user.ui.mapper.mapFromDomain
import eu.peernetwork.user.ui.model.UiAccount
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@User.Scope
class UserViewModel @Inject constructor(
    private val dispatcher: DispatcherProvider,
    private val usecase: AuthenticatedUserUsecase,
    private val descriptionUsecase: DescriptionUsecase
) : ViewModel() {
    private val mutableState = MutableStateFlow<State>(State.Loading)

    val state: StateFlow<State> = mutableState.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = State.Initialize
    )

    fun getAccount() {
        mutableState.tryEmit(State.Loading)
        viewModelScope.launch(dispatcher.io) {
            try {
                val response = usecase().mapFromDomain()
                val description = response.bio?.let { getDescription(it) }
                mutableState.tryEmit(State.Success(response.copy(
                    bio = description
                )))
            } catch (error: Throwable) {
                mutableState.tryEmit(State.Error(error))
            }
        }
    }

    private suspend fun getDescription(path: String): String? {
        return try {
            descriptionUsecase(path)
        } catch (_: Throwable) { null }
    }

    sealed interface State {
        data object Initialize : State
        data object Loading : State
        data class Success(val account: UiAccount) : State
        data class Error(val error: Throwable) : State
    }
}
