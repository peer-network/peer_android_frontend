package eu.peernetwork.app.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eu.peernetwork.persistence.domain.publishable.PublishableInteger
import eu.peernetwork.persistence.domain.retrievable.RetrievableInteger
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@Home.Scope
class HomeViewModel @Inject constructor(
    retrievableInteger: RetrievableInteger,
    private val publishableInteger: PublishableInteger
) : ViewModel() {
    private val mutableState = MutableStateFlow<State>(State.Initialize(retrievableInteger(TAG)))

    val state: StateFlow<State> = mutableState.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = mutableState.value
    )

    fun lastVisited(page: Int) {
        viewModelScope.launch { publishableInteger(TAG, page) }
    }

    sealed class State(val page: Int) {
        data class Initialize(val current: Int?): State(current ?: 0)
    }

    internal companion object {
        val TAG: String = HomeViewModel::class.java.name
    }
}
