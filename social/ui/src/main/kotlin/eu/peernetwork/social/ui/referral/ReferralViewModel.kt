package eu.peernetwork.social.ui.referral

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import eu.peernetwork.core.common.model.Pageable
import eu.peernetwork.social.domain.model.Invite
import eu.peernetwork.social.domain.usecase.InviteUsecase
import eu.peernetwork.social.ui.model.UiReferral
import eu.peernetwork.social.ui.usecase.ReferralPagingUsecase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

class ReferralViewModel @Inject constructor(
    private val usecase: ReferralPagingUsecase,
    private val inviteUsecase: InviteUsecase
) : ViewModel() {
    private val mutableState = MutableStateFlow<State>(State.Empty)

    private val mutableInviteState = MutableStateFlow<Status>(Status.Empty)

    val state: StateFlow<State> = mutableState.asStateFlow()

    val invite: StateFlow<Status> = mutableInviteState.asStateFlow()

    fun referral(userId:String, pageable: Pageable) {
        viewModelScope.launch {
            mutableState.emit(State.Loading)
            usecase(
                ReferralPagingUsecase.Parameter(userId, pageable)
            ).catch { mutableState.tryEmit(State.Error(it)) }
                .onStart { mutableState.tryEmit(State.Loading) }
                .cachedIn(viewModelScope)
                .apply {
                    collectLatest { mutableState.tryEmit(State.Success(this)) }
                }
        }
    }

    fun invite() {
        viewModelScope.launch {
            mutableInviteState.value = Status.Loading
            try {
                val result = inviteUsecase()
                mutableInviteState.value = Status.Success(result)
            } catch (error: Throwable) {
                mutableInviteState.value = Status.Error(error)
            }
        }
    }

    fun reset() {
        viewModelScope.launch {
            mutableState.tryEmit(State.Empty)
        }
    }

    sealed interface Status {
        data object Empty : Status
        data object Loading : Status
        data class Success(val invite: Invite) : Status
        data class Error(val error: Throwable) : Status
    }

    sealed interface State {
        data object Empty : State
        data object Loading : State
        data class Success(val content: Flow<PagingData<UiReferral>>) : State
        data class Error(val error: Throwable) : State
    }
}
