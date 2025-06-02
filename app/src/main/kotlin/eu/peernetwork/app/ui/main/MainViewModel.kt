package eu.peernetwork.app.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eu.peernetwork.user.domain.model.Token
import eu.peernetwork.user.domain.usecase.TokenObserverUsecase
import eu.peernetwork.user.domain.usecase.TokenUsecase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@Main.Scope
class MainViewModel @Inject constructor(
    tokenUsecase: TokenUsecase,
    tokenObserverUsecase: TokenObserverUsecase,
) : ViewModel() {
    val state: StateFlow<State> = tokenObserverUsecase()
        .map { token ->
            State(token)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = State(tokenUsecase())
        )

    data class State(val token: Token?)
}
