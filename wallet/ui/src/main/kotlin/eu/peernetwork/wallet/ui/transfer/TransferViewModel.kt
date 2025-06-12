package eu.peernetwork.wallet.ui.transfer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eu.peernetwork.wallet.domain.usecase.TransferUsecase
import eu.peernetwork.wallet.ui.mapper.mapFromDomain
import eu.peernetwork.wallet.ui.model.UiTransfer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.math.BigDecimal
import javax.inject.Inject

class TransferViewModel @Inject constructor(
    private val usecase: TransferUsecase
) : ViewModel() {
    private val mutableState = MutableStateFlow<State>(State.Empty)

    val state: StateFlow<State> = mutableState.asStateFlow()

    fun transfer(recipient: String, price: BigDecimal) {
        viewModelScope.launch {
            mutableState.tryEmit(State.Loading)
            try {
                val result = usecase(TransferUsecase.Parameter(recipient, price)).mapFromDomain()
                mutableState.tryEmit(State.Success(result))
            } catch (error: Throwable) {
                mutableState.tryEmit(State.Error(error))
            }
        }
    }

    fun reset() {
        viewModelScope.launch {
            mutableState.tryEmit(State.Empty)
        }
    }

    sealed interface State {
        data object Empty: State
        data object Loading: State
        data class Success(val transfer: UiTransfer): State
        data class Error(val error: Throwable): State
    }
}