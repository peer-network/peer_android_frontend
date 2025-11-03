package eu.peernetwork.user.ui.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eu.peernetwork.user.domain.usecase.ProtectedSettingsUsecase
import eu.peernetwork.user.domain.usecase.SettingsUsecase
import eu.peernetwork.user.ui.mapper.mapToModels
import eu.peernetwork.user.ui.model.UiAccount
import eu.peernetwork.user.ui.model.UiSettings
import eu.peernetwork.user.ui.usecase.ObserveAuthUserUsecase
import eu.peernetwork.user.ui.usecase.ProfileRefreshUsecase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@Account.Scope
class AccountViewModel @Inject constructor(
    private val refreshUsecase: ProfileRefreshUsecase,
    private val settingsUsecase: SettingsUsecase,
    private val protectedSettingsUsecase: ProtectedSettingsUsecase,
    private val observerUsecase: ObserveAuthUserUsecase
) : ViewModel() {
    private val _state = MutableStateFlow<State>(State.Default)

    private val _status = MutableStateFlow<Status>(Status.Default)

    val state: StateFlow<State> = _state.asStateFlow()

    val status: StateFlow<Status> = _status.asStateFlow()

    fun initialize() {
        viewModelScope.launch {
            observerUsecase().firstOrNull()?.let {
                _state.tryEmit(State.Success(it))
            } ?: get()
        }
    }

    fun get() {
        viewModelScope.launch {
            _state.tryEmit(State.Loading)
            try {
                val account = refreshUsecase()
                _state.tryEmit(State.Success(account))
            } catch (error: Throwable) {
                _state.tryEmit(State.Error(error))
            }
        }
    }

    fun update(account: UiAccount, update: List<UiSettings>, password: String) {
        _status.tryEmit(Status.Loading)
        viewModelScope.launch {
            try {
                handleUpdate(account, update, password)
                val account = refreshUsecase()
                _status.tryEmit(Status.Success(account))
                _state.tryEmit(State.Success(account))
            } catch (error: Throwable) {
                _status.tryEmit(Status.Error(error))
            }
        }
    }

    private suspend fun handleUpdate(
        account: UiAccount,
        model: List<UiSettings>,
        password: String
    ) {
        val mapper = account.mapToModels().associateBy { it.name }
        model.forEach {
            if (mapper[it.name]?.value != it.value) {
                it.value?.let { value ->
                    if (it.protected) {
                        protectedSettingsUsecase(ProtectedSettingsUsecase
                            .Parameter(it.name, value, password))
                    } else {
                        settingsUsecase(SettingsUsecase.Parameter(it.name, value))
                    }
                }
            }
        }
    }

    fun reset() { _state.tryEmit(State.Default) }

    sealed interface Status {
        data object Default : Status
        data object Loading : Status
        data class Success(val account: UiAccount) : Status
        data class Error(val error: Throwable) : Status
    }

    sealed interface State {
        data object Default : State
        data object Loading : State
        data class Success(val account: UiAccount) : State
        data class Error(val error: Throwable) : State
    }
}
