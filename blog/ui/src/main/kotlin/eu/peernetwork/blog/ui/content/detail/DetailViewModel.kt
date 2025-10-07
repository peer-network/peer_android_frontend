package eu.peernetwork.blog.ui.content.detail

import androidx.lifecycle.viewModelScope
import eu.peernetwork.blog.ui.model.UiPost
import eu.peernetwork.blog.ui.usecase.PostContentUsecase
import eu.peernetwork.media.core.interactor.ThumbnailInteractor
import eu.peernetwork.media.core.viewmodel.MediaViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class DetailViewModel @Inject constructor(
    interactor: ThumbnailInteractor,
    private val usecase: PostContentUsecase
) : MediaViewModel(interactor) {

    private val _state = MutableStateFlow<State>(State.Default)

    val state: StateFlow<State> = _state.asStateFlow()

    fun load(id: String) {
        viewModelScope.launch {
            _state.tryEmit(State.Loading)
            try {
                _state.tryEmit(State.Success(usecase(id)))
            } catch (error: Throwable) {
                _state.tryEmit(State.Error(error))
            }
        }
    }

    sealed interface State {
        data object Default : State
        data object Loading : State
        data class Success(val post: UiPost) : State
        data class Error(val error: Throwable) : State
    }
}
