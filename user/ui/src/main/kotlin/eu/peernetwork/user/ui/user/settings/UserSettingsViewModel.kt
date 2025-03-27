package eu.peernetwork.user.ui.user.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eu.peernetwork.user.domain.usecase.ProtectedSettingsUsecase
import eu.peernetwork.user.domain.usecase.SettingsUsecase
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

@UserSettings.Scope
class UserSettingsViewModel @Inject constructor(
    private val profileUsecase: ProfileUsecase,
    private val settingsUsecase: SettingsUsecase,
    private val protectedSettingsUsecase: ProtectedSettingsUsecase,
    observerUsecase: ObserveAuthUserUsecase
) : ViewModel() {
    private val mutableState = MutableStateFlow<State>(State.Initialize)

    val state: StateFlow<State> = mutableState.combine(
        observerUsecase()
    ) { state, account ->
        (state as? State.Success?)
            ?: account?.let { State.Success(it) } ?: state
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = State.Initialize
    )

    fun update(account: UiAccount, update: List<UserSettingsModel>, password: String) {
        viewModelScope.launch {
            try {
                val mapper = account.toSettings().associateBy { it.name }
                update.forEach {
                    if (mapper[it.name]?.value != it.value) {
                        it.value?.let { value ->
                            if (it.protected) {
                                protectedSettingsUsecase(
                                    ProtectedSettingsUsecase.Parameter(it.name, value, password)
                                )
                            } else {
                                settingsUsecase(SettingsUsecase.Parameter(it.name, value))
                            }
                        }
                    }
                }
                mutableState.tryEmit(State.Success(profileUsecase()))
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
