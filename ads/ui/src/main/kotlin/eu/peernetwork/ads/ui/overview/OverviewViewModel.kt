package eu.peernetwork.ads.ui.overview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eu.peernetwork.ads.domain.model.Filter
import eu.peernetwork.ads.domain.model.Metrics
import eu.peernetwork.ads.domain.usecase.MetricsUsecase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class OverviewViewModel @Inject constructor(
    private val metricsUsecase: MetricsUsecase
) : ViewModel() {
    private val _state = MutableStateFlow<State>(State.Default)

    val state: StateFlow<State> = _state.asStateFlow()

    fun getMetricsByAuthor(author: String) {
        viewModelScope.launch {
            try {
                _state.tryEmit(State.Loading)
                _state.tryEmit(State.Success(
                    metrics = metricsUsecase(Filter(author = author))
                ))
            } catch (error: Throwable) {
                _state.tryEmit(State.Error(error))
            }
        }
    }

    fun getMetricsByAds(id: String) {
        viewModelScope.launch {
            try {
                _state.tryEmit(State.Loading)
                _state.tryEmit(State.Success(
                    metrics = metricsUsecase(Filter(adsId = id))
                ))
            } catch (error: Throwable) {
                _state.tryEmit(State.Error(error))
            }
        }
    }

    sealed interface State {
        data object Default : State
        data object Loading : State

        data class Success(val metrics: Metrics) : State
        data class Error(val error: Throwable) : State
    }
}
