package eu.peernetwork.app.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eu.peernetwork.persistence.domain.observable.ObservableBoolean
import eu.peernetwork.persistence.domain.publishable.PublishableBoolean
import eu.peernetwork.user.domain.model.Token
import eu.peernetwork.user.domain.usecase.TokenObserverUsecase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel @Inject constructor(
    tokenObserverUsecase: TokenObserverUsecase,
    observableBoolean: ObservableBoolean,
    private val publishableBoolean: PublishableBoolean
) : ViewModel() {
    private val tag = this::class.java.name

    val state: StateFlow<State> = tokenObserverUsecase()
        .combine(observableBoolean(tag)) { token, showRegistration ->
            if (token != null) {
                State.Authenticated(token)
            } else {
                State.Startup(showRegistration == true)
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = State.Loading
        )

    fun showRegistration(show: Boolean) {
        viewModelScope.launch { publishableBoolean(tag, show) }
    }

    sealed interface State {
        data object Loading : State
        data class Startup(val isRegistration: Boolean) : State
        data class Authenticated(val token: Token): State
    }
}
