package eu.peernetwork.app.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eu.peernetwork.app.usecase.ResourceLoaderUsecase
import eu.peernetwork.app.usecase.VersionControlUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class SplashViewModel @Inject constructor(
    private val usecase: ResourceLoaderUsecase,
    private val versionControlUseCase: VersionControlUseCase
) : ViewModel() {
    private val mutableState = MutableStateFlow<State>(State.Empty)
    val state: StateFlow<State> = mutableState.asStateFlow()

    fun initialize() {
        viewModelScope.launch {
            mutableState.tryEmit(State.Loading)
            when (val result = versionControlUseCase()) {
                is VersionControlUseCase.Result.UpToDate -> {
                    try {
                        usecase()
                        mutableState.tryEmit(State.Ready)
                    } catch (error: Throwable) {
                        mutableState.tryEmit(State.Error(error))
                    }
                }
                is VersionControlUseCase.Result.Outdated -> {
                    mutableState.tryEmit(State.Outdated(result.url))
                }
                is VersionControlUseCase.Result.Error -> {
                    mutableState.tryEmit(State.Error(result.throwable))
                }
            }
        }
    }

    sealed interface State {
        data object Empty : State
        data object Loading : State
        data object Ready : State
        data class Error(val error: Throwable) : State
        data class Outdated(val url: String) : State
    }
}
