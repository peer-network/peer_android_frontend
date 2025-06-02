package eu.peernetwork.user.ui.settings.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eu.peernetwork.core.common.interactor.UrlInteractor
import eu.peernetwork.user.domain.usecase.DeactivationUsecase
import eu.peernetwork.user.domain.usecase.LogoutUsecase
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
    private val observerUsecase: ObserveAuthUserUsecase,
    private val logoutUsecase: LogoutUsecase,
    private val deactivationUsecase: DeactivationUsecase,
    private val urlInteractor: UrlInteractor
) : ViewModel() {
    private val mutableState = MutableStateFlow<State>(State.Empty)

    val state: StateFlow<State> = mutableState
        .combine(observerUsecase()) { state, account ->
            account?.let {
                State.Content(
                    account = account,
                    inviteUrl = String.format(urlInteractor.invite(), account.id),
                    processing = state is State.Loading,
                    error = (state as? State.Failure?)?.error
                )
            } ?: state
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = State.Empty
    )

    fun initialize() {
        viewModelScope.launch {
            if (observerUsecase().firstOrNull() == null) {
                getAccount()
            }
        }
    }

    fun getAccount() {
        mutableState.tryEmit(State.Loading)
        viewModelScope.launch {
            try {
                val account = refreshUsecase()
                val inviteLink = String.format(urlInteractor.invite(), account.id)
                mutableState.tryEmit(State.Content(account, inviteLink))
            } catch (error: Throwable) {
                mutableState.tryEmit(State.Failure(error))
            }
        }
    }

    fun update(account: UiAccount, update: List<UiSettings>, password: String) {
        mutableState.tryEmit(State.Loading)
        viewModelScope.launch {
            try {
                handleUpdate(account, update, password)
                val account = refreshUsecase()
                val inviteLink = String.format(urlInteractor.invite(), account.id)
                mutableState.tryEmit(State.Content(account, inviteLink))
            } catch (error: Throwable) {
                mutableState.tryEmit(State.Failure(error))
            }
        }
    }

    fun reset() { mutableState.tryEmit(State.Empty) }

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

    fun logout() {
        mutableState.tryEmit(State.Loading)
        viewModelScope.launch {
            try {
                logoutUsecase()
                mutableState.tryEmit(State.Empty)
            } catch (error: Throwable) {
                mutableState.tryEmit(State.Failure(error))
            }
        }
    }

    fun deactivate(password: String) {
        mutableState.tryEmit(State.Loading)
        viewModelScope.launch {
            try {
                deactivationUsecase(password)
                logoutUsecase()
                mutableState.tryEmit(State.Empty)
            } catch (error: Throwable) {
                mutableState.tryEmit(State.Failure(error))
            }
        }
    }

    sealed interface State {
        data object Empty : State
        data object Loading : State
        data class Content(
            val account: UiAccount,
            val inviteUrl: String,
            val processing: Boolean = false,
            val error: Throwable? = null
        ) : State
        data class Failure(val error: Throwable) : State
    }
}


