package eu.peernetwork.social.ui.member

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eu.peernetwork.social.domain.usecase.FollowUsecase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject

class MemberViewModel @Inject constructor(
    private val followUsecase: FollowUsecase
): ViewModel() {
    private val mutexes = ConcurrentHashMap.newKeySet<String>()
    private val mutableState = MutableStateFlow<State>(State.Default)
    private val followings = mutableMapOf<String, Boolean>()

    val state: StateFlow<State> = mutableState
        .map { state ->
            when (state) {
                is State.Loading,
                is State.Error,
                is State.Success,
                State.Default -> state
                else -> if (followings.isNotEmpty()) {
                    State.Content(
                        isLoading = state is State.Loading,
                        followings = followings.toMap(),
                        error = (state as? State.Error)?.error
                    )
                } else state
            }
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            State.Default
        )

    fun follow(userId: String) {
        if (!mutexes.add(userId)) return
        viewModelScope.launch {
            mutableState.value = State.Loading
            try {
                val isFollowing = followUsecase(userId)
                followings[userId] = isFollowing
                mutableState.emit(
                    State.Content(
                        isLoading = false,
                        followings = followings.toMap(),
                        error = null
                    )
                )
            } catch (e: Throwable) {
                mutableState.emit(State.Error(userId, e))
            } finally {
                mutexes.remove(userId)
            }
        }
    }

    sealed interface State {
        data object Default : State
        data object Loading : State
        data class Success(val userId: String, val isFollowing: Boolean) : State
        data class Content(
            val isLoading: Boolean,
            val followings: Map<String, Boolean>,
            val error: Throwable?
        ) : State
        data class Error(val userId: String, val error: Throwable) : State
    }
}