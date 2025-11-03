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
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.stateIn
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

    val state: StateFlow<State> = _state
        .combine(observerUsecase()) { state, account ->
            account?.let {
                State.Content(
                    account = account,
                    processing = state is State.Loading,
                    error = (state as? State.Error?)?.error
                )
            } ?: state
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = State.Default
    )

    fun initialize() {
        viewModelScope.launch {
            if (observerUsecase().firstOrNull() == null) {
                get()
            }
        }
    }

    fun get() {
        _state.tryEmit(State.Loading)
        viewModelScope.launch {
            try {
                val account = refreshUsecase()
                _state.tryEmit(State.Content(account))
            } catch (error: Throwable) {
                _state.tryEmit(State.Error(error))
            }
        }
    }

    fun update(account: UiAccount, update: List<UiSettings>, password: String) {
        _state.tryEmit(State.Loading)
        viewModelScope.launch {
            try {
                handleUpdate(account, update, password)
                val account = refreshUsecase()
                _state.tryEmit(State.Content(account))
            } catch (error: Throwable) {
                _state.tryEmit(State.Error(error))
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

    sealed interface State {
        data object Default : State
        data object Loading : State
        data class Content(
            val account: UiAccount,
            val processing: Boolean = false,
            val error: Throwable? = null
        ) : State
        data class Error(val error: Throwable) : State
    }
}
