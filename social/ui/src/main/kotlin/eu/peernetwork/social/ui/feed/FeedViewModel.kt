package eu.peernetwork.social.ui.feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eu.peernetwork.persistence.domain.observable.ObservableInteger
import eu.peernetwork.persistence.domain.publishable.PublishableInteger
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import javax.inject.Inject

class FeedViewModel @Inject constructor(
    observableInteger: ObservableInteger,
    private val publishableInteger: PublishableInteger
) : ViewModel() {
    private val tag = this::class.java.name

    val state: StateFlow<State> = observableInteger(tag).take(1).map {
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
