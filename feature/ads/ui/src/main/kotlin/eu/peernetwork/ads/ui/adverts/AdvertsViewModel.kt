package eu.peernetwork.ads.ui.adverts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import eu.peernetwork.ads.domain.model.Filter
import eu.peernetwork.ads.ui.model.UiCampaign
import eu.peernetwork.ads.ui.usecase.AdsPagingUsecase
import eu.peernetwork.core.common.paging.Pageable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

class AdvertsViewModel @Inject constructor(
    private val usecase: AdsPagingUsecase
) : ViewModel() {
    private val _state = MutableStateFlow<State>(State.Default)

    val state: StateFlow<State> = _state.asStateFlow()

    operator fun invoke(filter: Filter = Filter(), page: Pageable) {
        viewModelScope.launch {
            usecase(AdsPagingUsecase.Parameter(
                filter = filter,
                page = page
            )).catch { _state.tryEmit(State.Error(it)) }
                .onStart { _state.tryEmit(State.Loading) }
                .cachedIn(viewModelScope)
                .apply {
                    collectLatest { _state.tryEmit(State.Success(this)) }
                }
        }
    }

    sealed interface State {
        data object Default : State
        data object Loading : State

        data class Success(val content: Flow<PagingData<UiCampaign>>) : State
        data class Error(val error: Throwable) : State
    }
}
