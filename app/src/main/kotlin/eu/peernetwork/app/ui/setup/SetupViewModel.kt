package eu.peernetwork.app.ui.setup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eu.peernetwork.persistence.domain.observable.ObservableInteger
import eu.peernetwork.persistence.domain.publishable.PublishableInteger
import eu.peernetwork.persistence.domain.retrievable.RetrievableInteger
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@Setup.Scope
class SetupViewModel @Inject constructor(
    retrievableInteger: RetrievableInteger,
    observableInteger: ObservableInteger,
    private val publishableInteger: PublishableInteger
) : ViewModel() {
    val state: StateFlow<State> = observableInteger(TAG).map {
        State.Initialize(retrievableInteger(TAG))
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = State.Initialize(retrievableInteger(TAG))
    )

    fun lastVisited(page: Int) {
        viewModelScope.launch { publishableInteger(TAG, page) }
    }

    sealed class State(val page: Int) {
        data class Initialize(val current: Int?): State(current ?: 0)
    }

    internal companion object {
        val TAG: String = SetupViewModel::class.java.name
    }
}
