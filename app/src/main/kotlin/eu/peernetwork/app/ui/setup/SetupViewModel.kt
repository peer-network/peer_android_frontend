package eu.peernetwork.app.ui.setup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eu.peernetwork.persistence.domain.observable.ObservableBoolean
import eu.peernetwork.persistence.domain.publishable.PublishableBoolean
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

class SetupViewModel @Inject constructor(
    observableBoolean: ObservableBoolean,
    private val publishableBoolean: PublishableBoolean
) : ViewModel() {
    private val tag = this::class.java.name

    val state: StateFlow<State> = observableBoolean(tag)
        .map { isRegister ->
            if (isRegister == null || !isRegister) {
                State.Login
            } else {
                State.Register
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = State.Login
        )

    fun toggle(login: Boolean) {
        viewModelScope.launch { publishableBoolean(tag, login) }
    }

    sealed interface State {
        data object Login : State
        data object Register : State
    }
}
