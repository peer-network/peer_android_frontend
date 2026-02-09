package eu.peernetwork.ads.ui.descriptor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eu.peernetwork.ads.domain.model.Description
import eu.peernetwork.ads.domain.usecase.DescriptionUsecase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class DescriptorViewModel @Inject constructor(
    private val usecase: DescriptionUsecase
) : ViewModel() {
    private val _state = MutableStateFlow<State>(State.Default)

    val state: StateFlow<State> = _state.asStateFlow()

    operator fun invoke() {
        viewModelScope.launch {
            try {
                _state.tryEmit(State.Loading)
                _state.tryEmit(State.Success(usecase()))
            } catch (error: Throwable) {
                _state.tryEmit(State.Error(error))
            }
        }
    }

    sealed interface State {
        data object Default : State
        data object Loading : State

        data class Success(val description: Description): State
        data class Error(val error: Throwable) : State
    }
}
