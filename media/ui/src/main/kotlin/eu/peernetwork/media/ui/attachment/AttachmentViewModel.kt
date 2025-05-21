package eu.peernetwork.media.ui.attachment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eu.peernetwork.persistence.domain.observable.ObservableInteger
import eu.peernetwork.persistence.domain.publishable.PublishableInteger
import eu.peernetwork.persistence.domain.retrievable.RetrievableInteger
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class AttachmentViewModel @Inject constructor(
    private val retrievableInteger: RetrievableInteger,
    private val publishableInteger: PublishableInteger,
    private val observableInteger: ObservableInteger
) : ViewModel() {
    private val mutableState = MutableStateFlow<State>(State.Empty)

    val state: StateFlow<State> = mutableState.asStateFlow()

    fun initialize() {
        viewModelScope.launch {
            observableInteger(this::class.java.name)
                .collectLatest {
                    mutableState.tryEmit(State.Success(it ?: 0))
                }
        }
    }

    fun bump() {
        viewModelScope.launch {
            val tag = this::class.java.name
            val update = (retrievableInteger(tag) ?: 0) + 1
            publishableInteger(tag, update)
            mutableState.tryEmit(State.Success(update))
        }
    }

    sealed interface State {
        data object Empty : State
        data class Success(val counter: Int) : State
    }
}
