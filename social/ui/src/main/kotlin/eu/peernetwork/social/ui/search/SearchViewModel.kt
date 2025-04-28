package eu.peernetwork.social.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eu.peernetwork.social.domain.model.User
import eu.peernetwork.social.domain.usecase.UserSearchUsecase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@Search.Scope
class SearchViewModel @Inject constructor(
    private val userSearchUsecase: UserSearchUsecase
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _uiState = MutableStateFlow<SearchUiState>(SearchUiState.Empty)
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

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
}

sealed class SearchUiState {
    object Empty : SearchUiState()
    object Loading : SearchUiState()
    data class Success(val users: List<User>) : SearchUiState()
    data class Error(val message: String) : SearchUiState()
}