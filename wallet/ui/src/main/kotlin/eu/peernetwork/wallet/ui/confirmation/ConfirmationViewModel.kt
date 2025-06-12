package eu.peernetwork.wallet.ui.confirmation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eu.peernetwork.wallet.domain.usecase.OverviewUsecase
import eu.peernetwork.wallet.domain.usecase.QuoteUsecase
import eu.peernetwork.wallet.ui.mapper.mapFromDomain
import eu.peernetwork.wallet.ui.mapper.mapToDomain
import eu.peernetwork.wallet.ui.model.UiIntent
import eu.peernetwork.wallet.ui.model.UiQuote
import eu.peernetwork.wallet.ui.model.UiWallet
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class ConfirmationViewModel @Inject constructor(
    private val overviewUsecase: OverviewUsecase,
    private val quoteUsecase: QuoteUsecase
) : ViewModel() {
    private val mutableState = MutableStateFlow<State>(State.Empty)

    val state: StateFlow<State> = mutableState.asStateFlow()

    fun initialize(intent: UiIntent) {
        viewModelScope.launch {
            mutableState.tryEmit(State.Loading)
            try {
                mutableState.tryEmit(State.Success(
                    quoteUsecase(intent.mapToDomain()).mapFromDomain(),
                    overviewUsecase().mapFromDomain()
                ))
            } catch (error: Throwable) {
                mutableState.tryEmit(State.Error(error))
            }
        }
    }

    sealed interface State {
        data object Empty : State
        data object Loading : State
        data class Success(
            val quote: UiQuote,
            val wallet: UiWallet
        ) : State
        data class Error(val error: Throwable) : State
    }
}
