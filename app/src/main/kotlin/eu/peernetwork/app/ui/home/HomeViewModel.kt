package eu.peernetwork.app.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eu.peernetwork.persistence.domain.publishable.PublishableInteger
import eu.peernetwork.persistence.domain.retrievable.RetrievableInteger
import eu.peernetwork.user.domain.usecase.PrincipalUsecase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@Home.Scope
class HomeViewModel @Inject constructor(
    private val usecase: PrincipalUsecase,
    private val retrievableInteger: RetrievableInteger,
    private val publishableInteger: PublishableInteger,
) : ViewModel() {
    private val mutableState = MutableStateFlow<State>(State.Empty)

    val state: StateFlow<State> = mutableState.asStateFlow()

    operator fun invoke() {
        mutableState.tryEmit(State.Loading)
        viewModelScope.launch {
            try {
                val lastVisitedPage = retrievableInteger(TAG) ?: 0
                mutableState.tryEmit(State.Success(usecase(), lastVisitedPage))
            } catch (error: Throwable) {
                mutableState.tryEmit(State.Error(error))
            }
        }
    }

    fun lastVisited(page: Int) {
        viewModelScope.launch { publishableInteger(TAG, page) }
    }

    sealed interface State {
        data object Empty : State
        data object Loading : State
        data class Success(
            val userId: String,
            val lastVisitedPage: Int
        ) : State
        data class Error(val error: Throwable) : State
    }

    internal companion object {
        val TAG: String = HomeViewModel::class.java.name
    }
}
