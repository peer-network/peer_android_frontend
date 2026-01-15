package eu.peernetwork.social.ui.report

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eu.peernetwork.social.domain.usecase.ReportUsecase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class ReportViewModel @Inject constructor(
    private val usecase: ReportUsecase
) : ViewModel() {
    private val _state = MutableStateFlow<State>(State.Empty)

    val state: StateFlow<State> = _state.asStateFlow()

    operator fun invoke(id: String) {
        viewModelScope.launch {
            _state.tryEmit(State.Loading)
            try {
                _state.emit(State.Success(usecase(param = id)))
            } catch (error: Throwable) {
                _state.tryEmit(State.Error(error))
            }
        }
    }

    fun reset() {
        viewModelScope.launch {
            _state.tryEmit(State.Empty)
        }
    }

    sealed interface State {
        data object Empty : State
        data object Loading : State
        data class Success(val code: String) : State
        data class Error(val error: Throwable) : State
    }
}
