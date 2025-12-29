package eu.peernetwork.social.ui.report

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class ReportViewModel @Inject constructor() : ViewModel() {
    private val _state = MutableStateFlow<State>(State.Empty)

    val state: StateFlow<State> = _state.asStateFlow()

    operator fun invoke(id: String) {}

    sealed interface State {
        data object Empty : State
        data object Loading : State
        data object Success : State
        data class Error(val error: Throwable) : State
    }
}
