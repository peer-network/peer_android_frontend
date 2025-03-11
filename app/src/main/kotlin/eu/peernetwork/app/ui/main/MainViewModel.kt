package eu.peernetwork.app.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eu.peernetwork.user.domain.model.Token
import eu.peernetwork.user.domain.usecase.TokenObserverUsecase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

class MainViewModel @Inject constructor(
    tokenObserverUsecase: TokenObserverUsecase
) : ViewModel() {
    val state: StateFlow<State> = tokenObserverUsecase()
        .map { token ->
            if (token != null) {
                State.Authenticated(token)
            } else {
                State.Startup
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = State.Startup
        )

    sealed interface State {
        data object Startup : State
        data class Authenticated(val token: Token): State
    }
}
