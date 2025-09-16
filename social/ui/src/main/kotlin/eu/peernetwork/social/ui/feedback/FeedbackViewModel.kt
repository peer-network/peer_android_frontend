package eu.peernetwork.social.ui.feedback

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eu.peernetwork.persistence.domain.observable.ObservableLong
import eu.peernetwork.persistence.domain.publishable.PublishableLong
import eu.peernetwork.persistence.domain.retrievable.RetrievableLong
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

class FeedbackViewModel @Inject constructor(
    private val retrievableLong: RetrievableLong,
    private val publishableLong: PublishableLong,
    private val observableLong: ObservableLong
) : ViewModel() {
    private val mutableState = MutableStateFlow<State>(State.Loading)
    val state: StateFlow<State> = mutableState.asStateFlow()

    private val counterKey = this::class.java.name + "_counter"
    private val feedbackGivenKey = this::class.java.name + "_feedbackGiven"

    private val minInterval = 10_000L
    private val maxPopups = 3

    fun initialize() {
        viewModelScope.launch {
            val counter = retrievableLong(counterKey) ?: 0
            val feedbackGiven = retrievableLong(feedbackGivenKey) ?: 0
            if (feedbackGiven == 1L || counter >= maxPopups) {
                mutableState.emit(State.Disabled)
                return@launch
            }
            delay(minInterval)
            val newCounter = counter + 1
            publishableLong(counterKey, newCounter)
            mutableState.emit(State.Success(newCounter))

            if (newCounter >= maxPopups) {
                mutableState.emit(State.Disabled)
            }
        }
    }

    fun observe() {
        viewModelScope.launch {
            publishableLong(feedbackGivenKey, 1)
            mutableState.emit(State.Disabled)
        }
    }

    sealed interface State {
        data object Loading : State
        data class Success(val counter: Long) : State
        data object Disabled : State
    }
}
