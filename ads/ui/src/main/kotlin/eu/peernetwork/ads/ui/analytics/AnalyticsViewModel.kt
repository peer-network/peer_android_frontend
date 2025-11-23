package eu.peernetwork.ads.ui.analytics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eu.peernetwork.ads.ui.model.UiCampaign
import eu.peernetwork.ads.ui.usecase.AdsUsecase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class AnalyticsViewModel @Inject constructor(
    private val usecase: AdsUsecase
) : ViewModel() {
    private val _state = MutableStateFlow<State>(State.Default)

    val state: StateFlow<State> = _state.asStateFlow()

    operator fun invoke(id: String) {
        viewModelScope.launch {
            try {
                _state.tryEmit(State.Loading)
                _state.tryEmit(State.Success(usecase(id)))
            } catch (error: Throwable) {
                _state.tryEmit(State.Error(error))
            }
        }
    }

    sealed interface State {
        data object Default: State
        data object Loading: State
        data class Success(val ads: UiCampaign): State
        data class Error(val error: Throwable): State
    }
}
