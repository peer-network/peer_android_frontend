package eu.peernetwork.social.ui.member

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eu.peernetwork.social.domain.usecase.FollowUsecase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class MemberViewModel @Inject constructor(
    private val followUsecase: FollowUsecase
): ViewModel() {
    private val mutableState = MutableStateFlow<State>(State.Default)
    val state: StateFlow<State> = mutableState.asStateFlow()

    fun follow(userId: String) {
        viewModelScope.launch {
            mutableState.value = State.Loading
            runCatching {
                followUsecase(userId)
            }.onSuccess { isFollowing ->
                mutableState.value = State.Success(userId, isFollowing)
            }.onFailure { error ->
                mutableState.value = State.Error(error)
            }
        }
    }

    sealed interface State {
        data object Default: State
        data object Loading: State
        data class Success(val userId: String, val isFollowing: Boolean): State
        data class Error(val error: Throwable): State
    }
}