package eu.peernetwork.blog.ui.creator

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eu.peernetwork.blog.domain.model.Draft
import eu.peernetwork.blog.domain.usecase.ContentCreationUsecase
import eu.peernetwork.blog.ui.mapper.mapToPhoto
import eu.peernetwork.blog.ui.model.UiDraft
import eu.peernetwork.blog.ui.model.UiPost
import eu.peernetwork.core.common.usecase.TextEncoderUsecase
import eu.peernetwork.core.ui.usecase.AnnotationUsecase
import eu.peernetwork.media.core.model.MimeType
import eu.peernetwork.media.core.usecase.MediaEncoderUsecase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class CreatorViewModel @Inject constructor(
    private val usecase: ContentCreationUsecase,
    private val mediaEncoderUsecase: MediaEncoderUsecase,
    private val textEncoderUsecase: TextEncoderUsecase,
    private val annotationUsecase: AnnotationUsecase
) : ViewModel() {
    private val mutableState = MutableStateFlow<State>(State.Empty)

    val state: StateFlow<State> = mutableState.asStateFlow()

    fun create(draft: UiDraft) {
        mutableState.tryEmit(State.Loading)
        viewModelScope.launch {
            try {
                val model = draft.mapToDomain()
                val photo = usecase(model).mapToPhoto { annotationUsecase(it) }
                mutableState.tryEmit(State.Success(photo))
            } catch (error: Throwable) {
                mutableState.tryEmit(State.Error(error))
            }
        }
    }

    private fun UiDraft.mapToDomain(): Draft {
        val type = when(media) {
            MimeType.Photo -> Draft.Type.Image(attachments.mapNotNull { mediaEncoderUsecase(it) })
            MimeType.Video -> Draft.Type.Video(attachments.mapNotNull { mediaEncoderUsecase(it) })
            else -> Draft.Type.Text(listOf(textEncoderUsecase(description)))
        }
        return Draft(
            title = title,
            description = description,
            tags = description.hashtags(),
            type = type,
        )
    }

    private fun String.hashtags(): List<String> {
        return Regex("#\\w+").findAll(this)
            .map { it.value.replace("#", "") }
            .toList()
    }

    sealed interface State {
        data object Empty : State
        data object Loading : State
        data class Success(val content: UiPost): State
        data class Error(val error: Throwable): State
    }
}
