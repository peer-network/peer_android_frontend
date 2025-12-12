package eu.peernetwork.wallet.ui.confirmation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eu.peernetwork.wallet.domain.usecase.OverviewUsecase
import eu.peernetwork.wallet.domain.usecase.QuoteUsecase
import eu.peernetwork.wallet.domain.usecase.RewardUsecase
import eu.peernetwork.wallet.ui.mapper.mapFromDomain
import eu.peernetwork.wallet.ui.mapper.v2.mapToDomain
import eu.peernetwork.wallet.ui.model.v2.UiQuote
import eu.peernetwork.wallet.ui.model.v2.UiToken
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class ConfirmationViewModel @Inject constructor(
    private val overview: OverviewUsecase,
    private val rewardUsecase: RewardUsecase,
    private val quoteUsecase: QuoteUsecase
) : ViewModel() {
    private val _state = MutableStateFlow<State>(State.Empty)

    private var job: Job? = null

    val state: StateFlow<State> = _state.asStateFlow()

    operator fun invoke(token: UiToken) {
        job = viewModelScope.launch {
            _state.tryEmit(State.Loading)
            try {
                val quote = quoteUsecase(token.mapToDomain())
                val wallet = overview().mapFromDomain()
                val rewards = rewardUsecase().map { it.mapFromDomain() }.associateBy { it.name }
                _state.tryEmit(State.Success(
                    quote = UiQuote(
                        value = quote.value / wallet.rate.toBigDecimal(),
                        available = rewards[token.name]?.available ?: 0,
                        balance = wallet.balance
                    ),
                ))
            } catch (error: Throwable) {
                _state.tryEmit(State.Error(error))
            }
        }
    }

    fun reset() {
        job?.cancel()
        viewModelScope.launch {
            _state.emit(State.Empty)
        }
    }

    sealed interface State {
        data object Empty : State
        data object Loading : State
        data class Success(val quote: UiQuote) : State
        data class Error(val error: Throwable) : State
    }
}
