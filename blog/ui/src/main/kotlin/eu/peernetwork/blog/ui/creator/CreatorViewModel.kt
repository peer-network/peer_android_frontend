package eu.peernetwork.blog.ui.creator

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eu.peernetwork.blog.domain.model.Draft
import eu.peernetwork.blog.ui.model.UiDraft
import eu.peernetwork.blog.ui.model.UiPost
import eu.peernetwork.blog.ui.usecase.CreateUsecase
import eu.peernetwork.core.common.usecase.TextEncoderUsecase
import eu.peernetwork.media.core.model.UiMimeType
import eu.peernetwork.media.core.usecase.MediaEncoderUsecase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class CreatorViewModel @Inject constructor(
    private val usecase: CreateUsecase,
    private val mediaEncoderUsecase: MediaEncoderUsecase,
    private val textEncoderUsecase: TextEncoderUsecase
) : ViewModel() {
    private val mutableState = MutableStateFlow<State>(State.Empty)

    val state: StateFlow<State> = mutableState.asStateFlow()

    fun create(draft: UiDraft) {
        mutableState.tryEmit(State.Loading)
        viewModelScope.launch {
            try {
                val model = draft.mapToDomain()
                val photo = usecase(model)
                mutableState.tryEmit(State.Success(photo))
            } catch (error: Throwable) {
                mutableState.tryEmit(State.Error(error))
            }
        }
    }

    private fun UiDraft.mapToDomain(): Draft {
        val type = when(media) {
            UiMimeType.Photo -> Draft.Type.Image(attachments.mapNotNull { mediaEncoderUsecase(it) })
            UiMimeType.Video -> Draft.Type.Video(attachments.mapNotNull { mediaEncoderUsecase(it) })
            UiMimeType.Music -> Draft.Type.Audio(
                files = attachments.mapNotNull { mediaEncoderUsecase(it) },
                cover = cover?.let { uri ->
                    mediaEncoderUsecase(uri)?.let { listOf(it) }
                }
            )
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

    fun reset() {
        viewModelScope.launch {
            mutableState.tryEmit(State.Empty)
        }
    }

    sealed interface State {
        data object Empty : State
        data object Loading : State
        data class Success(val content: UiPost): State
        data class Error(val error: Throwable): State
    }
}
