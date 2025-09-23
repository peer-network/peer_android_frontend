package eu.peernetwork.app.ui.splash


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eu.peernetwork.app.usecase.LogDeviceModelUsecase
import eu.peernetwork.app.usecase.VersionUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class SplashViewModel @Inject constructor(
    private val versionUseCase: VersionUseCase,
    private val logDeviceModelUsecase: LogDeviceModelUsecase
) : ViewModel() {
    private val mutableState = MutableStateFlow<State>(State.Empty)

    val state: StateFlow<State> = mutableState.asStateFlow()

    fun initialize() {
        viewModelScope.launch {
            mutableState.tryEmit(State.Loading)
            runCatching {
                logDeviceModelUsecase()
            }.onFailure {
                mutableState.tryEmit(State.Error(it))
                return@launch
            }
            runCatching {
                versionUseCase()
                mutableState.tryEmit(State.Success)
            }.onFailure {
                mutableState.tryEmit(State.Error(it))
                return@launch
            }
        }
    }

    sealed interface State {
        data object Empty : State
        data object Loading : State
        data object Success : State
        data class Error(val error: Throwable) : State
    }
}
