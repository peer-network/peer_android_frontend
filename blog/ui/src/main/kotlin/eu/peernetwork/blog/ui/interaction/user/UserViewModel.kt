package eu.peernetwork.blog.ui.interaction.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import eu.peernetwork.blog.domain.model.Engagement
import eu.peernetwork.blog.ui.model.UiAuthor
import eu.peernetwork.blog.ui.usecase.InteractionUsecase
import eu.peernetwork.core.common.paging.Pageable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class UserViewModel @Inject constructor(
    private val usecase: InteractionUsecase
) : ViewModel() {
    private val _state = MutableStateFlow<Map<String, State>>(emptyMap())

    val state: StateFlow<Map<String, State>> = _state.asStateFlow()

    fun load(
        id: String,
        engagement: Engagement.Content,
        page: Pageable
    ) {
        val key = "$id/$engagement"
        viewModelScope.launch {
            usecase(
                InteractionUsecase.Parameter(
                    id = id,
                    engagement = engagement,
                    page = page,
                )
            ).catch { updateState(key, State.Error(it)) }
                .onStart { updateState(key, State.Loading) }
                .cachedIn(viewModelScope)
                .apply {
                    collectLatest { updateState(key, State.Success(id, this)) }
                }
        }
    }

    private fun updateState(key: String, state: State) {
        _state.update { it + (key to state) }
    }

    fun reset() {
        viewModelScope.launch {
            _state.value = emptyMap()
        }
    }

    sealed interface State {
        data object Empty : State
        data object Loading : State
        data class Success(
            val id: String,
            val content: Flow<PagingData<UiAuthor>>
        ) : State
        data class Error(val error: Throwable) : State
    }
}
