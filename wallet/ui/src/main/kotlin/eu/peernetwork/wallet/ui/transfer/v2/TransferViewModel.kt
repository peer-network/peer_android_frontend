package eu.peernetwork.wallet.ui.transfer.v2

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eu.peernetwork.wallet.domain.usecase.TransferUsecase
import eu.peernetwork.wallet.ui.mapper.mapFromDomain
import eu.peernetwork.wallet.ui.model.UiTransfer
import eu.peernetwork.wallet.ui.model.UiTransferDetail
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.math.BigDecimal
import javax.inject.Inject

class TransferViewModel @Inject constructor(
    private val usecase: TransferUsecase
) : ViewModel() {
    private val _status = MutableStateFlow<Status>(Status.Empty)

    private val _state = MutableStateFlow<State>(State.Empty)

    val state: StateFlow<State> = _state.asStateFlow()

    val status: StateFlow<Status> = _status.asStateFlow()

    fun transfer(recipient: String, price: BigDecimal, message: String? = null) {
        viewModelScope.launch {
            _state.tryEmit(State.Loading)
            try {
                val result = usecase(TransferUsecase.Parameter(
                    recipient = recipient,
                    tokens = price,
                    message = message
                )).mapFromDomain()
                _state.tryEmit(State.Success(result))
            } catch (error: Throwable) {
                _state.tryEmit(State.Error(error))
            }
        }
    }

    fun proceed(detail: UiTransferDetail) {
        viewModelScope.launch {
            _status.tryEmit(Status.Confirmation(detail))
        }
    }

    fun checkout(detail: UiTransferDetail) {
        viewModelScope.launch {
            _status.tryEmit(Status.Checkout(detail))
        }
    }

    fun reset() {
        viewModelScope.launch {
            _status.tryEmit(Status.Empty)
            _state.tryEmit(State.Empty)
        }
    }

    sealed interface Status {
        data object Empty: Status
        data class Confirmation(val detail: UiTransferDetail): Status
        data class Checkout(val detail: UiTransferDetail): Status
    }

    sealed interface State {
        data object Empty: State
        data object Loading: State
        data class Success(val transfer: UiTransfer): State
        data class Error(val error: Throwable): State
    }
}