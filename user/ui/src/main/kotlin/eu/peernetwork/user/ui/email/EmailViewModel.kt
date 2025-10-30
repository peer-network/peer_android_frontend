package eu.peernetwork.user.ui.email

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eu.peernetwork.user.domain.usecase.ProtectedSettingsUsecase
import eu.peernetwork.user.ui.model.UiSettings
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class EmailViewModel @Inject constructor(
    private val usecase: ProtectedSettingsUsecase,
) : ViewModel() {
    private val mutableState = MutableStateFlow<State>(State.Empty)

    val state: StateFlow<State> = mutableState.asStateFlow()

    fun update(email: String, password: String) {
        viewModelScope.launch {
            try {
                mutableState.tryEmit(State.Loading)
                usecase(ProtectedSettingsUsecase.Parameter(UiSettings.EMAIL, email, password))
                mutableState.tryEmit(State.Success(email))
            } catch (error: Throwable) {
                mutableState.tryEmit(State.Error(error))
            }
        }
    }

    sealed interface State {
        data object Empty : State
        data object Loading : State
        data class Success(val email: String) : State
        data class Error(val error: Throwable) : State
    }
}
