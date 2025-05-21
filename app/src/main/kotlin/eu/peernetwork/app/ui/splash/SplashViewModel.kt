package eu.peernetwork.app.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eu.peernetwork.app.usecase.ResourceUsecase
import eu.peernetwork.app.usecase.VersionUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class SplashViewModel @Inject constructor(
    private val usecase: ResourceUsecase,
    private val versionUseCase: VersionUseCase
) : ViewModel() {
    private val mutableState = MutableStateFlow<State>(State.Empty)
    val state: StateFlow<State> = mutableState.asStateFlow()

    fun initialize() {
        viewModelScope.launch {
            mutableState.tryEmit(State.Loading)
            when (val result = versionUseCase()) {
                is VersionUseCase.Result.UpToDate -> {
                    try {
                        usecase()
                        mutableState.tryEmit(State.Success())
                    } catch (error: Throwable) {
                        mutableState.tryEmit(State.Error(error))
                    }
                }
                is VersionUseCase.Result.Outdated -> {
                    mutableState.tryEmit(State.Success(result.url))
                }
                is VersionUseCase.Result.Error -> {
                    mutableState.tryEmit(State.Error(result.throwable))
                }
            }
        }
    }

    sealed interface State {
        data object Empty : State
        data object Loading : State
        data class Success(val update: String? = null) : State
        data class Error(val error: Throwable) : State
    }
}
