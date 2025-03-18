package eu.peernetwork.app.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eu.peernetwork.persistence.domain.publishable.PublishableBoolean
import eu.peernetwork.persistence.domain.retrievable.RetrievableBoolean
import eu.peernetwork.user.domain.model.Token
import eu.peernetwork.user.domain.usecase.TokenObserverUsecase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@Main.Scope
class MainViewModel @Inject constructor(
    tokenObserverUsecase: TokenObserverUsecase,
    retrievableBoolean: RetrievableBoolean,
    private val publishableBoolean: PublishableBoolean
) : ViewModel() {
    private val tag = this::class.java.name

    val state: StateFlow<State> = tokenObserverUsecase()
        .map { token ->
            if (token != null) {
                State.Home(token)
            } else {
                State.Startup(retrievableBoolean(tag) == true)
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = State.Splash
        )

    fun showRegistration(show: Boolean) {
        viewModelScope.launch { publishableBoolean(tag, show) }
    }

    sealed interface State {
        data object Splash : State
        data class Startup(val isRegistration: Boolean) : State
        data class Home(val token: Token): State
    }
}
