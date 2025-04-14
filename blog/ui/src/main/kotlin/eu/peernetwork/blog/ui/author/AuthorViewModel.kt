package eu.peernetwork.blog.ui.author

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eu.peernetwork.blog.domain.usecase.CurrentAuthorUsecase
import eu.peernetwork.blog.ui.mapper.mapFromDomain
import eu.peernetwork.blog.ui.model.UiAuthor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@Author.Scope
class AuthorViewModel @Inject constructor(
    private val usecase: CurrentAuthorUsecase
) : ViewModel() {
    private val mutableState = MutableStateFlow<State>(State.Empty)

    val state: StateFlow<State> = mutableState.asStateFlow()

    init { getAuthor() }

    fun getAuthor() {
        mutableState.tryEmit(State.Loading)
        viewModelScope.launch {
            try {
                mutableState.tryEmit(State.Success(usecase().mapFromDomain()))
            } catch (error: Throwable) {
                mutableState.tryEmit(State.Error(error))
            }
        }
    }

    sealed interface State {
        data object Empty : State
        data object Loading : State
        data class Success(val account: UiAuthor) : State
        data class Error(val error: Throwable) : State
    }
}
