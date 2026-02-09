package eu.peernetwork.wallet.ui.transactions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import eu.peernetwork.core.common.paging.Pageable
import eu.peernetwork.wallet.domain.model.Filter
import eu.peernetwork.wallet.domain.model.Sort
import eu.peernetwork.wallet.ui.model.UiTransaction
import eu.peernetwork.wallet.ui.usecase.TransactionListingUsecase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

class TransactionsViewModel @Inject constructor(
    private val usecase: TransactionListingUsecase
) : ViewModel() {
    private val _state = MutableStateFlow<State>(State.Default)

    private val _status = MutableStateFlow<Status>(Status.Empty)

    val state: StateFlow<State> = _state.asStateFlow()

    val status: StateFlow<Status> = _status.asStateFlow()

    operator fun invoke(
        page: Pageable,
        filter: Filter = Filter.None,
        sort: Sort = Sort.NEWEST,
    ) {
        viewModelScope.launch {
            usecase(
                param = TransactionListingUsecase.Parameter(
                    filter = filter,
                    sort = sort,
                    page = page
                )
            ).catch { _state.tryEmit(State.Error(it)) }
                .onStart { _state.tryEmit(State.Loading) }
                .cachedIn(viewModelScope)
                .apply { collectLatest {
                    _state.tryEmit(State.Success(this))
                } }
        }
    }

    fun updatedAt(timestamp: Long) {
        viewModelScope.launch {
            _status.tryEmit(Status.Success(timestamp))
        }
    }

    sealed interface Status {
        data object Empty : Status
        data object Loading : Status
        data class Success<T>(val data: T) : Status
        data class Error(val error: Throwable) : Status
    }

    sealed interface State {
        data object Default: State
        data object Loading: State
        data class Success(val content: Flow<PagingData<UiTransaction>>): State
        data class Error(val error: Throwable): State
    }
}
