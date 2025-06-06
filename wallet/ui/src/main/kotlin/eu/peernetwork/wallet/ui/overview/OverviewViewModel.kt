package eu.peernetwork.wallet.ui.overview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eu.peernetwork.wallet.domain.usecase.ObservableOverviewUsecase
import eu.peernetwork.wallet.domain.usecase.OverviewUsecase
import eu.peernetwork.wallet.ui.mapper.mapFromDomain
import eu.peernetwork.wallet.ui.model.UiWallet
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class OverviewViewModel @Inject constructor(
    private val usecase: OverviewUsecase,
    private val observer: ObservableOverviewUsecase
) : ViewModel() {
    private val mutableState = MutableStateFlow<State>(State.Empty)

    val state: StateFlow<State> = mutableState.asStateFlow()

    fun initialize() {
        viewModelScope.launch {
            observer().collectLatest {
                mutableState.tryEmit(State.Success(it.mapFromDomain()))
            }
        }
    }

    fun getBalance() {
        viewModelScope.launch {
            mutableState.tryEmit(State.Loading)
            try {
                mutableState.tryEmit(State.Success(usecase().mapFromDomain()))
            } catch (error: Throwable) {
                mutableState.tryEmit(State.Error(error))
            }
        }
    }

    sealed interface State {
        data object Empty : State
        data object Loading : State
        data class Success(val wallet: UiWallet) : State
        data class Error(val error: Throwable) : State
    }
}
