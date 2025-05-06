package eu.peernetwork.social.ui.followings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eu.peernetwork.core.common.model.Page
import eu.peernetwork.core.common.model.Pageable
import eu.peernetwork.social.domain.model.Member
import eu.peernetwork.social.domain.usecase.FollowingsUsecase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class FollowingsViewModel @Inject constructor(
    private val followingsUsecase: FollowingsUsecase
): ViewModel() {
    private val mutableState = MutableStateFlow<State>(State.Empty)
    val state: StateFlow<State> = mutableState.asStateFlow()

    fun followings(userId: String, pageable: Pageable) {
        viewModelScope.launch {
            val result = followingsUsecase(FollowingsUsecase.Params(userId, pageable))
            mutableState.value = State.Success(result)
        }
    }

    sealed interface State {
        data object Empty: State
        data class Success(val page: Page<Member>): State
    }
}