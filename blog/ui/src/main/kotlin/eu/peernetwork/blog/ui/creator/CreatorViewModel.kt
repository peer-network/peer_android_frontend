package eu.peernetwork.blog.ui.creator

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eu.peernetwork.blog.domain.model.Draft
import eu.peernetwork.blog.ui.model.UiDraft
import eu.peernetwork.blog.ui.model.UiPost
import eu.peernetwork.blog.ui.usecase.CreateUsecase
import eu.peernetwork.media.core.model.UiMimeType
import eu.peernetwork.media.core.model.UiOffset
import eu.peernetwork.media.core.usecase.MediaEncoderUsecase
import eu.peernetwork.media.core.usecase.TextEncoderUsecase
import eu.peernetwork.media.core.usecase.VideoEncoderUsecase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class CreatorViewModel @Inject constructor(
    private val usecase: CreateUsecase,
    private val videoEncoderUsecase: VideoEncoderUsecase,
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

    private suspend fun UiDraft.mapToDomain(): Draft {
        val media = if (attachment.files.isEmpty()) {
            UiMimeType.Text
        } else {
            attachment.media
        }
        val type = when(media) {
            UiMimeType.Photo -> Draft.Type.Image(attachment.files.mapNotNull {
                mediaEncoderUsecase(it.uri)
            })
            UiMimeType.Video -> Draft.Type.Video(attachment.files.mapNotNull {
                val offset = it.props.getParcelable<UiOffset?>(it.path) ?: UiOffset.None
                videoEncoderUsecase(
                    VideoEncoderUsecase.Parameter(
                        uri = it.uri,
                        offset = offset
                    )
                )
            })
            UiMimeType.Music -> Draft.Type.Audio(
                files = attachment.files.mapNotNull { mediaEncoderUsecase(it.uri) },
                cover = attachment.files.firstOrNull()?.cover?.let { uri ->
                    mediaEncoderUsecase(uri)
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
