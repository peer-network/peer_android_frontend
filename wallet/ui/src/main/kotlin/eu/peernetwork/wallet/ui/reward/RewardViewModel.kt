package eu.peernetwork.wallet.ui.reward

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eu.peernetwork.wallet.domain.usecase.ObservableRewardUsecase
import eu.peernetwork.wallet.domain.usecase.RewardUsecase
import eu.peernetwork.wallet.ui.mapper.mapFromDomain
import eu.peernetwork.wallet.ui.model.UiReward
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

class RewardViewModel @Inject constructor(
    private val usecase: RewardUsecase,
    observableUsecase: ObservableRewardUsecase
) : ViewModel() {
    private val mutableState = MutableStateFlow<State>(State.Loading)

    val state: StateFlow<State> = combine(
        observableUsecase(),
        mutableState
    ) { rewards, state ->
        if (rewards.isEmpty()) {
            state
        } else {
            State.Success(rewards.map { it.mapFromDomain() })
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = State.Initialize
    )

    fun getRewards() {
        mutableState.tryEmit(State.Loading)
        viewModelScope.launch {
            try {
                mutableState.tryEmit(State.Success(usecase().map { it.mapFromDomain() }))
            } catch (error: Throwable) {
                mutableState.tryEmit(State.Error(error))
            }
        }
    }

    sealed interface State {
        data object Initialize : State
        data object Loading : State
        data class Success(val rewards: List<UiReward>) : State
        data class Error(val error: Throwable) : State
    }
}
