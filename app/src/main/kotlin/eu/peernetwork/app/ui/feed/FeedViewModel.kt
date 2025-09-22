package eu.peernetwork.app.ui.feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eu.peernetwork.persistence.domain.observable.ObservableInteger
import eu.peernetwork.persistence.domain.publishable.PublishableInteger
import eu.peernetwork.persistence.domain.retrievable.RetrievableInteger
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@Feed.Scope
class FeedViewModel @Inject constructor(
    retrievableInteger: RetrievableInteger,
    observableInteger: ObservableInteger,
    private val publishableInteger: PublishableInteger
) : ViewModel() {

    val state: StateFlow<State> = combine(
        observableInteger(TAG),
        observableInteger(FILTER),
    ) { tag, filter -> Pair(tag, filter) }.map {
        State.Initialize(
            it.first,
            it.second
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = State.Initialize(
            retrievableInteger(TAG),
            retrievableInteger(FILTER)
        )
    )

    fun setFilter(filter: Int) {
        viewModelScope.launch { publishableInteger(FILTER, filter) }
    }

    fun lastVisited(page: Int) {
        viewModelScope.launch { publishableInteger(TAG, page) }
    }

    sealed class State(val page: Int) {
        data class Initialize(
            val current: Int?,
            val filter: Int?
        ): State(current ?: 0)
    }

    internal companion object {
        const val TAG: String = "eu.peernetwork.app.ui.feed.FeedViewModel::TAG"
        const val FILTER: String = "eu.peernetwork.app.ui.feed.FeedViewModel::FILTER"
    }
}
