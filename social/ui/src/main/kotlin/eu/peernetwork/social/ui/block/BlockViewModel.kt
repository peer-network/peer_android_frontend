package eu.peernetwork.social.ui.block

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eu.peernetwork.social.domain.usecase.BlockUsecase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class BlockViewModel @Inject constructor(
    private val usecase: BlockUsecase
): ViewModel() {
    private val mutableState = MutableStateFlow<State>(State.Empty)
    val state: StateFlow<State> = mutableState.asStateFlow()

    fun block(userId: String) {
        viewModelScope.launch {
            mutableState.tryEmit(State.Loading)
            try {
                usecase(param = userId)
                mutableState.emit(State.Success(true))
            } catch (error: Throwable) {
                mutableState.tryEmit(State.Error(error))
            }
        }
    }

    sealed interface State {
        data object Empty: State
        data object Loading: State
        data class Success(val status: Boolean): State
        data class Error(val error: Throwable): State
    }
}
