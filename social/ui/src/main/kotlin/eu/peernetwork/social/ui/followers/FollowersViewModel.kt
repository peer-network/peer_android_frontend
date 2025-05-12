package eu.peernetwork.social.ui.followers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eu.peernetwork.core.common.model.Page
import eu.peernetwork.core.common.model.Pageable
import eu.peernetwork.social.domain.model.Member
import eu.peernetwork.social.domain.usecase.FollowersUsecase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class FollowersViewModel @Inject constructor(
    private val followersUsecase: FollowersUsecase
): ViewModel() {
    private val mutableState = MutableStateFlow<State>(State.Empty)
    val state: StateFlow<State> = mutableState.asStateFlow()

    fun followers(userId: String, pageable: Pageable) {
        viewModelScope.launch {
            val result = followersUsecase(FollowersUsecase.Params(userId, pageable))
            mutableState.value = State.Success(result)
        }
    }

    sealed interface State {
        data object Empty: State
        data class Success(val page: Page<Member>): State
    }
}