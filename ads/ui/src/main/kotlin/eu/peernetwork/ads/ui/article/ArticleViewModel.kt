package eu.peernetwork.ads.ui.article

import androidx.lifecycle.viewModelScope
import eu.peernetwork.ads.domain.model.Content
import eu.peernetwork.ads.domain.usecase.ContentUsecase
import eu.peernetwork.media.core.interactor.ThumbnailInteractor
import eu.peernetwork.media.core.viewmodel.MediaViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.collections.plus

class ArticleViewModel @Inject constructor(
    interactor: ThumbnailInteractor,
    private val usecase: ContentUsecase
) : MediaViewModel(interactor) {
    private val _states = MutableStateFlow<Map<String, State>>(emptyMap())

    val states: StateFlow<Map<String, State>> = _states.asStateFlow()

    operator fun invoke(id: String) {
        viewModelScope.launch {
            try {
                updateState(id, State.Loading)
                updateState(id, State.Success(usecase(id)))
            } catch (error: Throwable) {
                updateState(id, State.Error(error))
            }
        }
    }

    private fun updateState(id: String, state: State) {
        _states.update { it + (id to state) }
    }

    sealed interface State {
        data object Default: State
        data object Loading: State
        data class Success(val content: Content): State
        data class Error(val error: Throwable): State
    }
}
