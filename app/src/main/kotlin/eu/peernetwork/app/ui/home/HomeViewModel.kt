package eu.peernetwork.app.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eu.peernetwork.persistence.domain.publishable.PublishableInteger
import eu.peernetwork.persistence.domain.retrievable.RetrievableInteger
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@Home.Scope
class HomeViewModel @Inject constructor(
    retrievableInteger: RetrievableInteger,
    private val publishableInteger: PublishableInteger
) : ViewModel() {
    private val tag = this::class.java.name

    val state: StateFlow<State> = MutableStateFlow(retrievableInteger(tag)).map {
        State.Ready(it ?: 0)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = State.Initialize
    )

    fun updateFeed(page: Int) {
        viewModelScope.launch { publishableInteger(tag, page) }
    }

    sealed interface State {
        data object Initialize: State
        data class Ready(val page: Int): State
    }
}
