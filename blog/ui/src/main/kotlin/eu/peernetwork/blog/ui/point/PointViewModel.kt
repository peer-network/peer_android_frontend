package eu.peernetwork.blog.ui.point

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eu.peernetwork.blog.domain.usecase.ObservePointUsecase
import eu.peernetwork.blog.domain.usecase.PointUsecase
import eu.peernetwork.blog.ui.mapper.mapFromDomain
import eu.peernetwork.blog.ui.model.UiPoint
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

class PointViewModel @Inject constructor(
    private val usecase: PointUsecase,
    observePointUsecase: ObservePointUsecase
) : ViewModel() {
    private val mutableState = MutableStateFlow<State>(State.Loading)

    val state: StateFlow<State> = combine(
        observePointUsecase(),
        mutableState
    ) { point, state ->
        if (point.isEmpty()) {
            state
        } else {
            State.Success(point.map { it.mapFromDomain() })
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = State.Initialize
    )

    fun getPoints() {
        mutableState.tryEmit(State.Loading)
        viewModelScope.launch {
            try {
                mutableState.tryEmit(State.Success(usecase().map { it.mapFromDomain() }))
            } catch (error: Throwable) {
                mutableState.tryEmit(State.Error(error))
            }
        }
    }

    sealed interface State {
        data object Initialize : State
        data object Loading : State
        data class Success(val points: List<UiPoint>) : State
        data class Error(val error: Throwable) : State
    }
}
