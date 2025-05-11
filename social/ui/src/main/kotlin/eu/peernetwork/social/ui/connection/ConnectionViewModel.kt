package eu.peernetwork.social.ui.connection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eu.peernetwork.social.domain.usecase.ConnectionsUsecase
import eu.peernetwork.social.domain.usecase.ObserveConnectionsUsecase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class ConnectionViewModel @Inject constructor(
    private val connectionsUsecase: ConnectionsUsecase,
    private val observeConnectionsUsecase: ObserveConnectionsUsecase
) : ViewModel() {
    private val mutableState = MutableStateFlow<State>(State.Empty)

    private val mutableConnections = MutableStateFlow<Map<String, Boolean>>(emptyMap())

    val state: StateFlow<State> = mutableState.asStateFlow()

    val connections: StateFlow<Map<String, Boolean>> = mutableConnections.asStateFlow()

    fun initialize() {
        viewModelScope.launch {
            observeConnectionsUsecase().collectLatest {
                mutableConnections.emit(it)
            }
        }
    }

    fun connect(id: String) {
        viewModelScope.launch {
            mutableState.emit(State.Loading)
            runCatching {
                connectionsUsecase(id)
                mutableState.emit(State.Success)
            }.onFailure {
                    mutableState.emit(State.Error(it))
                }
        }
    }

    fun reset() {
        viewModelScope.launch {
            mutableState.emit(State.Empty)
        }
    }

    sealed interface State {
        object Empty : State
        object Loading : State
        object Success : State
        data class Error(val error: Throwable) : State
    }
}
