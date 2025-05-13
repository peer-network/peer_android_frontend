package eu.peernetwork.social.ui.peers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eu.peernetwork.core.common.model.Page
import eu.peernetwork.core.common.model.Pageable
import eu.peernetwork.social.domain.model.Member
import eu.peernetwork.social.domain.usecase.FollowingsUsecase
import eu.peernetwork.social.domain.usecase.PeersUsecase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class PeersViewModel @Inject constructor(
    private val peersUsecase: PeersUsecase
): ViewModel() {
    private val mutableState = MutableStateFlow<State>(State.Empty)
    val state: StateFlow<State> = mutableState.asStateFlow()

    fun peers(pageable: Pageable) {
        viewModelScope.launch {
            try {
                val result = peersUsecase(PeersUsecase.Params(pageable))
                mutableState.value = State.Success(result)
            } catch (e: Throwable) {
                mutableState.value = State.Error(e)
            }

        }
    }

    sealed interface State {
        data object Empty: State
        data class Success(val page: Page<Member>): State
        data class Error(val error: Throwable): State
    }
}