package eu.peernetwork.app.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eu.peernetwork.user.domain.model.User
import eu.peernetwork.user.domain.usecase.ObserveUserSearchUsecase
import eu.peernetwork.user.domain.usecase.UserSearchUsecase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@Search.Scope
class SearchViewModel @Inject constructor(
    private val userSearchUsecase: UserSearchUsecase,
    private val observeUserSearchUsecase: ObserveUserSearchUsecase
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _uiState = MutableStateFlow<SearchUiState>(SearchUiState.Empty)
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    init {
        observeSearchResults()
    }

    fun searchUsers(query: String) {
        _searchQuery.value = query
        viewModelScope.launch {
            _uiState.value = SearchUiState.Loading
            try {
                userSearchUsecase.invoke(query)
            } catch (e: Exception) {
                _uiState.value = SearchUiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    private fun observeSearchResults() {
        observeUserSearchUsecase.invoke()
            .onEach { users ->
                _uiState.value = if (users.isEmpty()) {
                    SearchUiState.Empty
                } else {
                    SearchUiState.Success(users)
                }
            }
            .launchIn(viewModelScope)
    }
}

sealed class SearchUiState {
    object Empty : SearchUiState()
    object Loading : SearchUiState()
    data class Success(val users: List<User>) : SearchUiState()
    data class Error(val message: String) : SearchUiState()
}