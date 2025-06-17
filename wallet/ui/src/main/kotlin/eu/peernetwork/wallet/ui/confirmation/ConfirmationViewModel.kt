package eu.peernetwork.wallet.ui.confirmation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eu.peernetwork.wallet.domain.usecase.OverviewUsecase
import eu.peernetwork.wallet.domain.usecase.QuoteUsecase
import eu.peernetwork.wallet.domain.usecase.RewardUsecase
import eu.peernetwork.wallet.ui.mapper.mapFromDomain
import eu.peernetwork.wallet.ui.mapper.mapToDomain
import eu.peernetwork.wallet.ui.model.UiToken
import eu.peernetwork.wallet.ui.model.UiQuote
import eu.peernetwork.wallet.ui.model.UiReward
import eu.peernetwork.wallet.ui.model.UiWallet
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
    private val mutableState = MutableStateFlow<State>(State.Empty)

    val state: StateFlow<State> = mutableState.asStateFlow()

    fun initialize(token: UiToken) {
        viewModelScope.launch {
            mutableState.tryEmit(State.Loading)
            try {
                mutableState.tryEmit(State.Success(
                    quoteUsecase(token.mapToDomain()).mapFromDomain(),
                    overview().mapFromDomain(),
                    rewardUsecase().map { it.mapFromDomain() }
                ))
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
        data object Empty : State
        data object Loading : State
        data class Success(
            val quote: UiQuote,
            val wallet: UiWallet,
            val rewards: List<UiReward>
        ) : State
        data class Error(val error: Throwable) : State
    }
}
